package com.spnetty.netty;

import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by
 * User: djyin
 * Date: 2/22/14
 * Time: 11:47 AM
 * 连接池。
 * 正常情况下，如果服务器和客户端均为netty的NIO实现，一般情况下是不需要连接池的，因为这种客户端都是异步，不等待应答的,1-2个netty连接就能将网络跑满。
 * 但在另一种情况下，如果服务器是OIO的实现，这种情况下就需要使用连接池了。
 * 典型的配合方式
 * 1.UnsafeNettyClient+NettyClientPool+OIO的服务器
 * 2.NettyClient+NIO的服务器
 * 单终究使用连接池可以一定程度上避免网络重连的成本。
 */
public abstract class NettyClientPool<T extends NettyClient> {

    private static final Logger log = LoggerFactory.getLogger(NettyClientPool.class);

    protected String host = "localhost";

    protected Integer port = 8080;

    protected Integer maxIdle = 20;

    protected Integer minIdle = 10;

    protected Integer maxTotal = 100;

    protected Integer maxWaitMillis = 0;

    protected boolean testOnBorrow = true;

    protected boolean lifo = true;

    protected boolean testOnReturn = false;

    protected boolean blockWhenExhausted = true;


    // 工作线程数
    protected int workers = 0;

    // 整个池公用的woker线程
    private EventLoopGroup workerGroup = null;

    // 连接超时,默认是 maxWaitMillis 的 1/3
    private int connectTimeoutMillis = 300;

    private boolean tcpNoDelay = false;

    private Map<ChannelOption<?>, Object> channelOptions = null;


    private ObjectPool<T> pool;


    public void shutdown() {
        log.debug("Client Pool begin to Shutdown.");
        this.pool.close();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        log.debug("Client Pool fully Shutdown.");
    }

    /**
     * 初始化过程和构造函数分开,避免spring注入属性时失败
     */
    public synchronized void init() {
        // 线程数设置
        if (getWorkerGroup() == null) {
            setWorkerGroup(new NioEventLoopGroup(getWorkers()));
        }
        if (getChannelOptions() == null) {
            setChannelOptions(new HashMap<ChannelOption<?>, Object>());
        }
        getChannelOptions().put(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(getConnectTimeoutMillis()));
        getChannelOptions().put(ChannelOption.TCP_NODELAY, getTcpNoDelay());

        if (getMaxWaitMillis() < 1) {
            setMaxWaitMillis(getConnectTimeoutMillis() * 3 + 1);
        }
        // 连接设置
        PooledObjectFactory<T> factory = new ClientPoolFactory();
        // 连接池设置
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        // 纠正参数中可能的错误
        // 参数中 minIdel < maxIdle < maxTotal
        int maxIdle = getMaxIdle() < getMinIdle() ? getMinIdle() : getMaxIdle();
        int maxTotal = getMaxTotal() < getMaxIdle() ? getMaxIdle() : getMaxTotal();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(getMinIdle());
        poolConfig.setMaxTotal(maxTotal);

        poolConfig.setMaxWaitMillis(getMaxWaitMillis());

        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setLifo(lifo);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setBlockWhenExhausted(blockWhenExhausted);

        this.pool = new GenericObjectPool<T>(factory, poolConfig);
        log.info("NettyClientPool init with {} NIO Threads, ready to connect {}:{}", getWorkers(), getHost(), getPort());

    }


    /**
     * Gets client.
     *
     * @return the client
     * @throws Exception the exception
     */
    public T getClient() throws Exception {
        log.trace("pool --> client: active:{}, idle:{}", pool.getNumActive(), pool.getNumIdle());
        return this.pool.borrowObject();
    }

    // 创建方法
    public abstract T create() throws Exception;

    /**
     * Return client.
     *
     * @param client the client
     * @throws Exception the exception
     */
    public void returnClient(T client) {
        try {
            log.trace("client --> pool: active:{}, idle:{}", pool.getNumActive(), pool.getNumIdle());
            this.pool.returnObject(client);
        } catch (Exception e) {
            log.error("client --> pool:", e);
        }
    }

    public Integer getNumActive() {
        return this.pool.getNumActive();
    }

    public Integer getNumIdle() {
        return this.pool.getNumIdle();
    }

    public Map<ChannelOption<?>, Object> getChannelOptions() {
        return this.channelOptions;
    }

    private class ClientPoolFactory implements PooledObjectFactory<T> {

        ClientPoolFactory() {
            super();
        }

        // 创建
        @Override
        public PooledObject<T> makeObject() throws Exception {
            T client = create();
            if (client.ready()) {
                log.trace("pool --c--> client: active:{}, idle:{}", pool.getNumActive(), pool.getNumIdle());
                return new DefaultPooledObject<T>(client);
            } else {
                throw new IllegalStateException("Netty client can't be create, for it can't be ready.");
            }
        }

        // 销毁
        @Override
        public void destroyObject(PooledObject<T> p)
                throws Exception {
            log.trace("client --d--> pool: active:{}, idle:{}", pool.getNumActive(), pool.getNumIdle());
            p.getObject().close();
        }

        // 获取前会被调用，在validateObject之前
        @Override
        public void activateObject(PooledObject<T> p) throws Exception {
            T client = p.getObject();
            if (client.isActive()) { // 快速检查
            } else if (client.ready()) { // 完整检查
            } else { // 快速失败
                throw new IllegalStateException("Netty client can't be create, for it can't be ready.");
            }
        }

        @Override
        public void passivateObject(PooledObject<T> p) throws Exception {
            // not necessary
        }

        // 验证，只有当设置setTestOnBorrow为true时被调用
        @Override
        public boolean validateObject(PooledObject<T> p) {
            return p.getObject().isActive();
        }


    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getWorkers() {
        return workers;
    }

    public void setWorkers(Integer workers) {
        this.workers = workers;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isLifo() {
        return lifo;
    }

    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public Integer getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

}
