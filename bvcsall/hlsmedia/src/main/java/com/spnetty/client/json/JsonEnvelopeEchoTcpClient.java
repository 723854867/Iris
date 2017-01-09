package com.spnetty.client.json;

import com.spnetty.client.PropertiesCtxLauncherUtils;
import com.spnetty.codec.Envelope;
import com.spnetty.codec.Heartbeat;
import com.spnetty.codec.json.FastjsonEnvelopeDecoder;
import com.spnetty.netty.AbstractNettyClient;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by djyin on 14-6-3.
 */
public class JsonEnvelopeEchoTcpClient extends AbstractNettyClient {

    public JsonEnvelopeEchoTcpClient(String host, int port, int workers, Map channelOptions) {
        super(host, port, workers, channelOptions);
    }

    final static AtomicLong inboundCounter = new AtomicLong(0);

    @Override
    public ChannelInitializer<SocketChannel> initChannel() {

        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast("logging", new LoggingHandler(LogLevel.WARN));

                // 理论上,心跳主要是由服务器ping客户端,大压力情况下,服务器是不会主动ping客户端的
                // 间隔时间,是连接超时的1000倍
                final int beatMillis = getConnectTimeoutMillis() * 1000;
                p.addLast("idleStateHandler", new IdleStateHandler(0, 0, beatMillis, TimeUnit.MILLISECONDS));
                // 处理心跳.模仿xmpp的心跳,发送时间延迟,随成功次数增加间隔
                // 成功次数
                final AtomicInteger combo = new AtomicInteger(0);
                // 记录上一次发送的时间
                final AtomicLong lastSentAckMillis = new AtomicLong(0);
                p.addLast("heartBeatHandler", new ChannelDuplexHandler() {
                    @Override
                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                        // 定时发送心跳消息
                        if (evt instanceof IdleStateEvent) {
                            int c = combo.get();
                            // 使用间隔判断,会导致,正常数据通讯结束后,第一次判断心跳超时,会让心跳包多发一次,但我觉得不是很影响,总比每次记录正常数据通信时间要好...
                            if (c > 5 && c < 11) { // 连续5次成功,间隔升级成 2 倍
                                if (System.currentTimeMillis() - lastSentAckMillis.get() > beatMillis * 2) {
                                    // goto send
                                } else {
                                    return;
                                }
                            } else if (c > 10) { //连续10次成功,间隔升级为 5 倍
                                if (System.currentTimeMillis() - lastSentAckMillis.get() > beatMillis * 5) {
                                    // goto send
                                } else {
                                    return;
                                }
                            }
                            // 发送心跳请求 HeartBeat ACK
                            Envelope env = new Envelope(new Heartbeat(Heartbeat.Type.ACK));
                            ctx.channel().writeAndFlush(env);
                            lastSentAckMillis.set(System.currentTimeMillis());
                        }
                        super.userEventTriggered(ctx, evt);

                    }
                });

                p.addLast("frameEncoder", new LengthFieldPrepender(8));
                p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
                p.addLast("decoder", new FastjsonEnvelopeDecoder());


                // 解封envelope
                p.addLast("extractor", new SimpleChannelInboundHandler<Envelope>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Envelope msg) throws Exception {
                        Object body = msg.getBody();
                        ctx.fireChannelRead(body);
                    }
                });
                // 处理心跳
                p.addLast("heartBeatResponseHandler", new SimpleChannelInboundHandler<Heartbeat>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Heartbeat in) throws Exception {
                        Heartbeat heartBeat = in;
                        switch (heartBeat.getType()) {
                            case ACK:
                                // 响应服务器主动发起的心跳
                                Envelope env = new Envelope(new Heartbeat(Heartbeat.Type.SYNC));
                                ctx.channel().writeAndFlush(env);
                                break;
                            case SYNC:
                                // 接收到客户端主动心跳的响应
                                // 计算连击, 成功条件是在连接超时时间的10倍内返回
                                if (System.currentTimeMillis() - lastSentAckMillis.get() > getConnectTimeoutMillis() * 10) {
                                    combo.addAndGet(1);
                                } else {
                                    combo.set(0);
                                }
                                break;
                        }
                    }
                });

            }
        };
    }

    public static void main(String[] args) throws IOException {
        final Runnable task = new Runnable() {
            long lastIn = 0;
            long lastTime = 0;

            @Override
            public void run() {
                try {
                    long thisIn = inboundCounter.get();
                    long thisTime = System.currentTimeMillis();
                    long time = thisTime - lastTime;
                    System.out.println(String.format("[stat] inbounds %d ",  (thisIn - lastIn) * 1000 / time));
                    lastIn = thisIn;
                    lastTime = thisTime;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

        String cfg = "classpath:/client.properties";
        Properties properties = PropertiesCtxLauncherUtils.load(cfg);
        String host = PropertiesCtxLauncherUtils.getHost(properties);
        Integer port = PropertiesCtxLauncherUtils.getPort(properties);
        Integer workers = PropertiesCtxLauncherUtils.getWorkers(properties);
        @SuppressWarnings("unchecked")
        Map<ChannelOption<?>, Object> channelOptions = PropertiesCtxLauncherUtils.channelOptions(properties);
        try {
            JsonEnvelopeEchoTcpClient client = new JsonEnvelopeEchoTcpClient(host, port, workers, channelOptions);
            client.init();
            for (int i = 0; i < 10000000; i++) {
                client.sendNextAndFlush("hello world ! fk=" + i);
            }
            System.out.println("*****************************");
            System.out.println("*          job done         *");
            System.out.println("*****************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
