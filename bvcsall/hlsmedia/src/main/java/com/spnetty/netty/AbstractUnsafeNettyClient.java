package com.spnetty.netty;

import com.spnetty.util.UriUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by User: djyin Date: 2/24/14 Time: 4:00 PM
 * 特征和AbstractNettyClient一致，但是不保证发送过程的线程安全，即不可以几个线程同时调用一个client实例发送数据。
 * 适用于连接吃的情况，每个线程在独享一个NettyClient
 */
public abstract class AbstractUnsafeNettyClient<T> implements NettyClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractUnsafeNettyClient.class);

    /**
     * 配置信息 *
     */
    protected String host;

    protected int port;

    protected String url;

    protected URI uri;

    private int connectTimeoutMillis = 300;

    private int workers;

    private Map<ChannelOption<?>, Object> channelOptions;


    /**
     * 内部属性
     */
    public enum ClientState {
        /**
         * 需要连接的初始状态
         */
        CONNECT,
        /**
         * 可用状态
         */
        READY,
        /**
         * 需要重新连接状态
         */
        RECONNECT,
        /**
         * 销毁状态
         */
        DESTORY,
        /**
         * 需要关闭连接状态
         */
        DISCONNECT
    }

    // 使用内部的状态替代掉 netty channel 内部的状态，以便于增加几个中间状态
    protected volatile ClientState nextState = ClientState.CONNECT;

    private final String name;

    protected Bootstrap bootstrap;

    protected EventLoopGroup workerGroup;

    protected volatile Channel channel;

    private ChannelFuture connectFuture;

    protected static final long CONNECT_EXCEPTION_WAIT = 200;

    private static AtomicInteger threadCounter = new AtomicInteger(0);

    /**
     * SPIRNG环境中需要的构造函数
     *
     * @throws URISyntaxException
     */
    @SuppressWarnings("unchecked")
    public AbstractUnsafeNettyClient() {
        super();
        this.name = String.format(getClass().getSimpleName() + "-%s", AbstractUnsafeNettyClient.threadCounter.incrementAndGet());
    }

    /**
     * URL should be of format tcp://host:port?option='value',option='value'
     *
     * @throws URISyntaxException
     */
    @SuppressWarnings("unchecked")
    public AbstractUnsafeNettyClient(final String url) throws URISyntaxException {

        super();
        URI u = UriUtils.getURI(url);
        this.uri = u;
        this.url = url;
        this.host = UriUtils.getHost(u);
        this.port = UriUtils.getPort(u);
        this.name = String.format(getClass().getSimpleName() + "-%s", AbstractUnsafeNettyClient.threadCounter.incrementAndGet());

    }

    /**
     * URL should be of format tcp://host:port?option='value',option='value'
     *
     * @throws URISyntaxException
     */
    @SuppressWarnings("unchecked")
    public AbstractUnsafeNettyClient(final String url, final int workers, Map<ChannelOption<?>, Object> channelOptions) throws URISyntaxException {

        super();
        URI u = UriUtils.getURI(url);
        this.uri = u;
        this.url = url;
        this.host = UriUtils.getHost(u);
        this.port = UriUtils.getPort(u);
        this.workers = workers;
        this.channelOptions = channelOptions;
        if (channelOptions.containsKey(ChannelOption.CONNECT_TIMEOUT_MILLIS)) {
            connectTimeoutMillis = 3 * (Integer) channelOptions.get(ChannelOption.CONNECT_TIMEOUT_MILLIS); //重试3次
        }
        this.name = String.format(getClass().getSimpleName() + "-%s", AbstractUnsafeNettyClient.threadCounter.incrementAndGet());

    }

    /**
     * ip / port
     */
    @SuppressWarnings("unchecked")
    public AbstractUnsafeNettyClient(final String host, final int port, final int workers, Map<ChannelOption<?>, Object> channelOptions) {

        super();
        this.workers = workers;
        this.host = host;
        this.port = port;
        this.url = String.format("tcp://$s:$s", host, port);
        this.uri = UriUtils.getURI(this.url);
        this.channelOptions = channelOptions;
        if (channelOptions.containsKey(ChannelOption.CONNECT_TIMEOUT_MILLIS)) {
            connectTimeoutMillis = 3 * (Integer) channelOptions.get(ChannelOption.CONNECT_TIMEOUT_MILLIS); //重试3次
        }
        this.name = String.format(getClass().getSimpleName() + "-%d", AbstractUnsafeNettyClient.threadCounter.incrementAndGet());

    }

    /**
     * 初始化
     */
    public void init() {
        this.nextState = ClientState.CONNECT;
        if (workerGroup == null) {
            workerGroup = new NioEventLoopGroup(this.workers);
        }
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(workerGroup);
        this.bootstrap.channel(NioSocketChannel.class);
        if (channelOptions != null) {
            for (Map.Entry<ChannelOption<?>, Object> entry : channelOptions.entrySet()) {
                this.bootstrap.option((ChannelOption<Object>) entry.getKey(), entry.getValue());
            }
            if (channelOptions.containsKey(ChannelOption.CONNECT_TIMEOUT_MILLIS)) {
                if (this.connectTimeoutMillis < 1) {
                    this.connectTimeoutMillis = 3 * (Integer) channelOptions.get(ChannelOption.CONNECT_TIMEOUT_MILLIS); //重试3次
                }
            }

        }
        this.bootstrap.handler(initChannel());
        // 通用监控连接中断情况
        // 如果连接中断,则channel的state会变成not registered
    }

    /**
     * 必须实现的初始化方法,在这里添加返回消息的响应
     */
    public abstract ChannelInitializer<SocketChannel> initChannel();


    public String getName() {
        return name;
    }

    /**
     * 检查客户端是否立即可以用
     *
     * @return the boolean
     */
    public boolean isActive() {
        return this.nextState.equals(ClientState.READY) && channel.isActive();
    }

    /**
     * 建立连接,准备好
     *
     * @return the boolean
     */
    @Override
    public boolean ready() {
        return _ready(connectTimeoutMillis * 3);
    }

    /**
     * 关闭连接 关闭连接的NettyClient,只需要再次调用
     */
    @Override
    public void close() {
        _setupDisconnect();
        if (this.channel != null) {
            this.channel.close();
        }
    }

    /**
     * 关闭这个client
     */
    @Override
    public void destory() {
        _setupDestory();
        if (this.channel != null) {
            this.channel.close();
        }
    }

    @Override
    public void await() {
        if (nextState.equals(ClientState.READY) && connectFuture != null) {
            connectFuture.awaitUninterruptibly();
        }
    }

    /**
     * 自检，达到正常能发送数据的状态，退出状态或者超时
     */
    protected boolean _ready(long timeout) throws IllegalStateException {
        if (nextState == ClientState.DESTORY) { // 有此状态的是一个待销毁对象
            // return false;
            throw new IllegalStateException(String.format("can't get ready for Unexpected state: %s", nextState));
        }
        long nanoLimit = TimeUnit.MILLISECONDS.toNanos(timeout);
        long lastTime = System.nanoTime();
        ClientState clientState = nextState;
        while (clientState != ClientState.DESTORY) {
            switch (clientState) {
                case CONNECT: {
                    boolean finishTrying = this._connectOrContinueConnecting();
                    if (finishTrying) {
                        nextState = ClientState.READY;
                    }
                    break;
                }
                case READY: {
                    // 有效状态，可以发送数据
                    // 检查连接状态
                    if (channel.isActive()) { // 正确状态
                        return true;
                    } else { // 暴力重连
                        nextState = ClientState.RECONNECT;
                    }
                }
                case RECONNECT: {
                    boolean finishTrying = this._disconnectOrContinueDisconnecting();
                    if (finishTrying) {
                        nextState = ClientState.CONNECT;
                    } else {
                        nextState = ClientState.RECONNECT;
                    }
                    break;
                }
                case DISCONNECT: {
                    boolean finishTrying = this._disconnectOrContinueDisconnecting();
                    if (finishTrying) {
                        return false;
                    } else {
                        nextState = ClientState.DESTORY;
                    }

                    break;
                }
                case DESTORY: {
                    // 无效状态，直接退出
                    break;
                }
                default: {
                    // If this ever happens, it's because we did something dumb and
                    // added a client state that we're
                    // not actually handling.
                    throw new IllegalStateException(String.format("Unexpected state: %s", nextState));
                }
            }
            // 判断是否超时
            if (!nextState.equals(ClientState.READY)) {
                // 超时,直接退出
                long now = System.nanoTime();
                long consumed = now - lastTime;
                if (consumed > nanoLimit) {
                    log.warn(String.format("%s timeout while getting connection ready for %s.", this.getName(), this.nextState));
                    return false;
                }
            }
            clientState = nextState;
        }
        return false;
    }

    // 尝试重连
    protected boolean _connectOrContinueConnecting() {
        log.debug(String.format("%s beginning connection process.", this.getName()));
        this.channel = null;
        connectFuture = this.bootstrap.connect(this.host, this.port);
        try {
            connectFuture.syncUninterruptibly();
        } catch (Exception e) {
            // ignored all exception
            log.error(String.format("%s failed to connect to server %s : %d, for exception occured.", this.getName(), this.host, this.port), e);
            // connectFuture。
            connectFuture.channel().close();
        }
        if (connectFuture.isSuccess()) {
            this.channel = connectFuture.channel();
            return true;
        } else {
            if (connectFuture.channel().isOpen()) {
                connectFuture.channel().close();
            }
            this.channel = null;
            log.error(String.format("%s failed to establish a channel to server %s : %d.", this.getName(), this.host, this.port));

            // Pause here to avoid needlessly burning resources if there's no
            // network connection at all
            try {
                Thread.sleep(CONNECT_EXCEPTION_WAIT);
            } catch (InterruptedException e) {
                // ignored
            }
            return false;
        }
    }

    // 尝试关闭
    protected boolean _disconnectOrContinueDisconnecting() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close();
            log.debug(String.format("%s waiting for connection to close.", this.getName()));
            connectFuture = this.channel.closeFuture();
            try {
                connectFuture.syncUninterruptibly();
            } catch (Exception e) {
                // ignored all exception
                log.warn("fail to disconnect", e);
            }
            if (connectFuture.cause() != null) {
                log.info(String.format("%s failed to cleanly close its connection.", this.getName()), this.channel.closeFuture().cause());
                try {
                    Thread.sleep(CONNECT_EXCEPTION_WAIT);
                } catch (InterruptedException e) {
                    // ignored
                }
                return false;
            } else {
                this.channel = null;
                return true;
            }
        } else {
            this.channel = null;
            return true;
        }
    }

    /**
     * 同步发送的方式
     *
     * @param msg the msg
     */
    public ChannelFuture sendNextSync(final T msg) {
        if (!_check()) return null;
        ChannelFuture future = this.channel.writeAndFlush(msg);
        future.syncUninterruptibly();
        return future;
    }

    /**
     * 发送消息，需要配合flush函数使用
     *
     * @param msg the notification
     */
    public ChannelFuture sendNext(final T msg) {
        if (!_check()) return null;
        // TODO 增加消息发送失败重试机制
        ChannelFuture future = this.channel.write(msg);
        return future;

    }

    /**
     * 发送消息。
     *
     * @param msg the notification
     */
    public ChannelFuture sendNextAndFlush(final T msg) {
        if (!_check()) return null;
        ChannelFuture future = this.channel.writeAndFlush(msg);
        return future;

    }

    /**
     * 将数据从nio的buff刷到socket缓冲区
     */
    public void flush() {
        if (this.channel != null)
            this.channel.flush();
    }


    /**
     * 设置成重连状态
     */
    protected void _setupReconnect() {
        this.nextState = ClientState.RECONNECT;
    }

    /**
     * 设置需要成重连状态
     */
    protected void _setupDisconnect() {
        this.nextState = ClientState.DISCONNECT;
    }

    /**
     * 设置成销毁状态
     */
    protected void _setupDestory() {
        this.nextState = ClientState.DESTORY;
    }

    // 检查接口状态
    protected boolean _check() {
        if (isActive()) {
            return true;
        } else if (ready()) {
            return true;
        }
        log.error(String.format("%s is not ready.", getName()));
        return false;
    }


    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }


    public Map<ChannelOption<?>, Object> getChannelOptions() {
        return channelOptions;
    }

    public void setChannelOptions(Map<ChannelOption<?>, Object> channelOptions) {
        this.channelOptions = channelOptions;
    }

    public int getWorkers() {
        return workers;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    @Override
    public String toString() {
        return "AbstractUnsafeNettyClient{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
