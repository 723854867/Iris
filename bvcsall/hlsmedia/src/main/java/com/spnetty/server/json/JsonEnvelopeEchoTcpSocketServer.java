package com.spnetty.server.json;

import com.spnetty.codec.Envelope;
import com.spnetty.codec.Heartbeat;
import com.spnetty.codec.json.FastjsonEnvelopeDecoder;
import com.spnetty.server.TcpSocketServer;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by djyin on 14-5-26.
 * 基于Json的socket服务器
 */
public class JsonEnvelopeEchoTcpSocketServer extends TcpSocketServer {
    private static final Logger log = LoggerFactory.getLogger(JsonEnvelopeEchoTcpSocketServer.class);

    final AtomicInteger ctxCounter = new AtomicInteger(0);
    final AtomicLong inboundCounter = new AtomicLong(0);
    final AtomicLong outboundCounter = new AtomicLong(0);

    @Override
    public ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();

                p.addLast("logging", new LoggingHandler(LogLevel.WARN));
                p.addLast("counter", new ChannelHandlerAdapter() {
                    @Override
                    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                        ctxCounter.addAndGet(1);
                    }

                    @Override
                    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                        ctxCounter.addAndGet(-1);
                    }
                });
                final int beatMillis = timeout * 1000 * 5;
                //控制心跳
                p.addLast("idleStateHandler", new IdleStateHandler(0, 0, beatMillis,
                        TimeUnit.MILLISECONDS));
                final AtomicInteger waitSync = new AtomicInteger(0);
                p.addLast("heartbeat", new ChannelDuplexHandler() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                        // 定时发送心跳消息
                        if (evt instanceof IdleStateEvent) {
                            // 如果心跳没有返回3次,关闭连接
                            if (waitSync.get() > 2) {
                                log.info(String.format("%s 's connection is closed for heartbeat timeout", ctx.name()));
                                ctx.close();
                            } else {
                                //发送一次
                                Envelope env = new Envelope(new Heartbeat(Heartbeat.Type.SYNC));
                                ctx.channel().writeAndFlush(env);
                                waitSync.addAndGet(1);
                                log.debug(String.format("%s 's connection will receive a heartbeat ack", ctx.name()));
                            }
                        }
                        super.userEventTriggered(ctx, evt);
                    }
                });

                // 最暴力的,基于bytes/s的流速控制,需要放到encoder之前
                //p.addLast("trafficShaping", new ChannelTrafficShapingHandler(0, 1000));

                p.addLast("frameEncoder", new LengthFieldPrepender(8));
                p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
                // decoder and encoder
                p.addLast("coder", new FastjsonEnvelopeDecoder());

                // 基于消息数目的,需要放到encoder之后
                p.addLast("trafficShaping", new com.spnetty.netty.traffic.ChannelTrafficShapingHandler(10, 10));

                // 优化

                final String HEARTBEAT_CNAME = Heartbeat.class.getCanonicalName();

                p.addLast("handler", new SimpleChannelInboundHandler<Envelope>() {
                    protected void channelRead0(ChannelHandlerContext ctx, Envelope msg) throws Exception {
                        if (msg.getCls().equals(HEARTBEAT_CNAME)) {
                            return;
                        }
                        inboundCounter.addAndGet(1);
                        // print
                        //System.out.println(msg);
                        // echo back
                        ctx.channel().write(msg).channel().flush();
                        outboundCounter.addAndGet(1);
                    }
                });

                // 响应对方心跳
                p.addLast("heartBeatResponder", new SimpleChannelInboundHandler<Envelope>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Envelope in) throws Exception {
                        if (in.getCls().equals(HEARTBEAT_CNAME)) {
                            Heartbeat hb = (Heartbeat) in.getBody();
                            switch (hb.getType()) {
                                case ACK:
                                    Envelope env = new Envelope(new Heartbeat(Heartbeat.Type.SYNC));
                                    ctx.channel().writeAndFlush(env);
                                    log.debug(String.format("%s 's connection will receive a heartbeat sync", ctx.name()));
                                    break;
                                case SYNC:
                                    waitSync.set(0);
                                    log.debug(String.format("%s 's connection has sent a heartbeat sync", ctx.name()));
                                    break;
                                default:

                            }
                        }
                    }
                });
            }
        };


    }

    @Override
    public void run() throws Exception {
        final Runnable task = new Runnable() {
            long lastIn = 0;
            long lastOut = 0;
            long lastTime = 0;

            @Override
            public void run() {
                try {
                    long thisIn = inboundCounter.get();
                    long thisOut = outboundCounter.get();
                    long thisTime = System.currentTimeMillis();
                    long time = thisTime - lastTime;
                    System.out.println(String.format("[stat] connections: %d, inbounds %d, outbounds %d ", ctxCounter.get(), (thisIn - lastIn) * 1000 / time, (thisOut - lastOut) * 1000 / time));
                    lastIn = thisIn;
                    lastOut = thisOut;
                    lastTime = thisTime;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        super.run();

    }
}
