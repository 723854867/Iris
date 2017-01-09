package com.spnetty.server;

import com.spnetty.util.CtxUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 默认的服务器设置，使用函数的方式便于覆盖
 * 默认的参数设置为优化小数据量，高并发的应用
 */
@Component
public abstract class TcpSocketServer {

    private static final Logger log = LoggerFactory.getLogger(TcpSocketServer.class);
    @Value("${netty.epoll}")
    protected boolean epoll = false;

    @Value("${netty.so.port}")
    protected int port;

    @Value("${netty.so.timeout}")
    protected int timeout;

    @Value("${netty.so.keepalive}")
    protected boolean keepAlive;

    @Value("${netty.so.backlog}")
    protected int backlog;

    @Value("${netty.tcp.nodelay}")
    protected boolean nodelay;

    @Value("${netty.so.sendbuf}")
    protected int sendbuf;

    @Value("${netty.so.receivebuf}")
    protected int receivebuf;

    @Value("${netty.so.reuseaddr}")
    protected boolean reuseaddr;

    @Value("${netty.so.linger}")
    protected int solinger;

    @Value("${netty.heartbeat}")
    protected int heartbeat;


    @Value("${netty.boss.threads}")
    private int bosses = 2;
    @Value("${netty.worker.threads}")
    private int workers = 8;
    @Value("${netty.worker.ioRatio}")
    private int ioRatio = 50;

    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workerLoopGroup;
    private Lock bLock = new ReentrantLock(true);
    private Lock wLock = new ReentrantLock(true);

    public EventLoopGroup bossGroup() {
        bLock.lock();
        if (bossLoopGroup == null) {
            if (epoll) {
                //只能设置成0，否者会出现多重监听的异常
                bossLoopGroup = new EpollEventLoopGroup(bosses);
            } else {
                bossLoopGroup = new NioEventLoopGroup(bosses);
            }
        }
        log.info("[TCP-SERVER] create EventLoopGroup with {} bosses", bosses);
        bLock.unlock();
        return bossLoopGroup;
    }

    public EventLoopGroup workerGroup() {
        wLock.lock();
        if (workerLoopGroup == null) {
            if (epoll) {
                workerLoopGroup = new EpollEventLoopGroup(workers);
            } else {
                workerLoopGroup = new NioEventLoopGroup(workers);
            }
        }
        log.info("[TCP-SERVER] create EventLoopGroup with {} workers", workers);

        wLock.unlock();
        return workerLoopGroup;
    }


    public Class<? extends ServerSocketChannel> channelClass() {
        // 根据操作系统，linux系列使用 EpollServerSocketChannel，其他系列使用 NioServerSocketChannel.class
        if (epoll) {
            log.info("[TCP-SERVER] use epoll channel");
            return EpollServerSocketChannel.class;
        } else {
            log.info("[TCP-SERVER] use poll channel");
            return NioServerSocketChannel.class;
        }


    }

    public abstract ChannelInitializer<SocketChannel> channelInitializer();

    public void init() throws Exception {
        // empty
        if (!SystemUtils.IS_OS_LINUX) {
            epoll = false;
        }
        // 尝试通过属性修改监听端口
        String nPort = System.getProperty("PORT");
        if (nPort != null) {
            setPort(Integer.valueOf(nPort));
        }
    }

    // 启动服务器
    public void run() throws Exception {
        try {
            init();
            ServerBootstrap b = bootstrap();
            b.childHandler(channelInitializer());
            ChannelFuture cf = b.bind(getPort()).sync();
            int pid = CtxUtils.pid();
            log.info("[TCP-SERVER] [PID] {} now listening [PORT] {} ...", pid, port);
            cf.channel().closeFuture().sync();

        } finally {
            shutdown();
        }
    }

    /**
     * 获取一些默认的channel配置
     *
     * @return the map
     */
    public Map<ChannelOption<?>, Object> channelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, isKeepAlive());
        //options.put(ChannelOption.SO_BACKLOG, getBacklog());
        options.put(ChannelOption.TCP_NODELAY, isNodelay());
        options.put(ChannelOption.SO_SNDBUF, getSendbuf());
        options.put(ChannelOption.SO_RCVBUF, getReceivebuf());
        //options.put(ChannelOption.SO_TIMEOUT, getTimeout());
        options.put(ChannelOption.SO_REUSEADDR, isReuseaddr());
        options.put(ChannelOption.SO_LINGER, getSolinger());
        return options;
    }

    /**
     * 获取默认的ServerBootstrap
     *
     * @return the server bootstrap
     */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public ServerBootstrap bootstrap() {
        ServerBootstrap sb = new ServerBootstrap();
        sb.group(bossGroup(), workerGroup());
        sb.channel(channelClass())
                .childHandler(new LoggingHandler(LogLevel.INFO));
        // options
        Map<ChannelOption<?>, Object> options = channelOptions();
        Set<ChannelOption<?>> keySet = options.keySet();
        for (ChannelOption option : keySet) {
            sb.option(option, options.get(option));
            sb.childOption(option, options.get(option));
        }
        return sb;
    }

    // 关闭服务器
    public void shutdown() {
        bossGroup().shutdownGracefully();
        workerGroup().shutdownGracefully();
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public boolean isNodelay() {
        return nodelay;
    }

    public void setNodelay(boolean nodelay) {
        this.nodelay = nodelay;
    }

    public int getReceivebuf() {
        return receivebuf;
    }

    public void setReceivebuf(int receivebuf) {
        this.receivebuf = receivebuf;
    }

    public int getSendbuf() {
        return sendbuf;
    }

    public void setSendbuf(int sendbuf) {
        this.sendbuf = sendbuf;
    }

    public boolean isReuseaddr() {
        return reuseaddr;
    }

    public void setReuseaddr(boolean reuseaddr) {
        this.reuseaddr = reuseaddr;
    }

    public Integer getSolinger() {
        return solinger;
    }

    public void setSolinger(Integer solinger) {
        this.solinger = solinger;
    }
}
