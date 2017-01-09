package com.spnetty.akka;

import com.alibaba.fastjson.JSONArray;
import com.spnetty.client.PropertiesCtxLauncherUtils;
import com.spnetty.codec.Envelope;
import com.spnetty.codec.Heartbeat;
import com.spnetty.codec.json.FastjsonEnvelopeDecoder;
import com.spnetty.netty.AbstractNettyClient;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by djyin on 14-6-3.
 */
public class ChatTcpClient extends AbstractNettyClient {

    public ChatTcpClient(String host, int port, int workers, Map channelOptions) {
        super(host, port, workers, channelOptions);
    }

    final static AtomicLong inboundCounter = new AtomicLong(0);
    final static AtomicLong outboundCounter = new AtomicLong(0);

    @Override
    public ChannelInitializer<SocketChannel> initChannel() {

        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                //p.addLast("logging", new LoggingHandler(LogLevel.WARN));
                // 理论上,心跳主要是由服务器ping客户端,大压力情况下,服务器是不会主动ping客户端的
                // 间隔时间,是连接超时的1000倍
                final int beatMillis = 15000;//getConnectTimeoutMillis() * 1000;

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
                        inboundCounter.addAndGet(1);
                        ctx.fireChannelRead(body);
                    }
                });

                p.addLast("handleOut", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        promise.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                outboundCounter.addAndGet(1);
                            }
                        });
                        super.write(ctx, msg, promise);
                    }

                });


                p.addLast("chatResponseHandler", new SimpleChannelInboundHandler<ChatMsg>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ChatMsg in) throws Exception {
                        ChatMsg chatMsg = in;
                        String fromWhoUID = chatMsg.getFromWhoUID();
                        String fromWhoName = chatMsg.getFromWhoName();
                        String toWhoUID = chatMsg.getToWhoUID();
                        String toWhoName = chatMsg.getToWhoName();
                        if (toWhoUID.equals(uid)) {
                            switch (chatMsg.getContentType()) {
                                case txt:
                                    System.out.println(String.format("   %s --> : %s", fromWhoName, chatMsg.getContent()));
                                    break;
                                case jpg:
                            }
                        } else if (fromWhoUID.equals(uid)) {
                            if (!chatMsg.getResult().equals(ChatMsg.ResultType.ok)) {
                                switch (chatMsg.getContentType()) {
                                    case txt:
                                        System.err.println(String.format("    --> %s :[%s] %s ", toWhoName, chatMsg.getResult(), chatMsg.getContent()));
                                        break;
                                    case jpg:
                                }
                            }

                        }
                        if (chatWith != null) {
                            System.out.println(uname + " --> " + chatWithName + " >");
                        }

                    }
                });
                p.addLast("cmdResponseHandler", new SimpleChannelInboundHandler<CommandMsg>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, CommandMsg in) throws Exception {
                        CommandMsg cmd = in;
                        switch (cmd.getType()) {
                            case login:
                                //记录当前会话信息
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    uid = (String) cmd.getAddtions()[0];
                                    uname = (String) cmd.getAddtions()[1];
                                    System.out.println(String.format("user %s logined with id %s", uname, uid));
                                } else {
                                    System.err.println(cmd.getContent());
                                }
                                break;
                            case logout:
                                break;
                            case username:
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    uname = (String) cmd.getAddtions()[0];
                                } else {
                                    System.err.println(cmd.getContent());
                                }
                                break;
                            case datetime:
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    Date now = (Date) cmd.getAddtions()[0];
                                    System.out.println(now);
                                }
                                break;
                            case disconnect:
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    System.out.println("disconnect form server..........");
                                }
                                break;
                            case findByUsername:
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    chatWith = (String) cmd.getAddtions()[0];
                                    chatWithName = (String) cmd.getAddtions()[1];
                                    System.out.println(uname + " --> " + chatWithName + " >");
                                } else if (cmd.getResult().equals(CommandMsg.RESULT_NULL)) {
                                    String username = (String) cmd.getAddtions()[0];
                                    System.err.println(String.format(" %s is not exist.", username));
                                }
                                break;
                            case users:
                                if (cmd.getResult().equals(CommandMsg.RESULT_OK)) {
                                    System.out.print("user list: ");
                                    // 这里是fastjson的一个局限性
                                    JSONArray users = (JSONArray) cmd.getAddtions()[0];
                                    int c = 0;
                                    for (Object username : users) {
                                        c++;
                                        System.out.print("           ");
                                        System.out.print(username);
                                        System.out.print(" ");
                                        if (c > 1) {
                                            System.out.println();
                                            c = 0;
                                        }
                                    }
                                }
                                break;
                            default:
                        }

                    }
                });
                p.addLast("systemResponseHandler", new SimpleChannelInboundHandler<SystemMsg>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, SystemMsg in) throws Exception {
                        SystemMsg cmd = in;
                        System.err.println(cmd);
                    }
                });
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
                // 监听中断
                p.addLast("disconnectDetector", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        System.err.println(String.format("disconnect from %s with id %s",
                                ctx.channel().remoteAddress(), uname));
                        // 清理数据
                        chatWith = null;
                        chatWithName = null;
                        uid = null;
                        uname = null;
                        super.channelUnregistered(ctx);
                    }
                });
            }
        };
    }

    static volatile boolean exited = false;
    // uid
    static volatile String uid = null;
    static volatile String uname = null;
    static volatile String chatWith = null;
    static volatile String chatWithName = null;

    public static void main(String[] args) throws IOException {

        String cfg = "classpath:/client.properties";
        Properties properties = PropertiesCtxLauncherUtils.load(cfg);
        String host = PropertiesCtxLauncherUtils.getHost(properties);
        Integer port = PropertiesCtxLauncherUtils.getPort(properties);
        Integer workers = PropertiesCtxLauncherUtils.getWorkers(properties);
        @SuppressWarnings("unchecked")
        Map<ChannelOption<?>, Object> channelOptions = PropertiesCtxLauncherUtils.channelOptions(properties);
        try {
            // 尝试通过属性修改监听端口
            String nPort = System.getProperty("PORT");
            if (nPort != null) {
                port = (Integer.valueOf(nPort));
            }
            String nHost = System.getProperty("HOST");
            if (nHost != null) {
                host = nHost;
            }
            final ChatTcpClient client = new ChatTcpClient(host, port, workers, channelOptions);
            //启动一个线程,不停的读取命令行输入
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    while (!exited) {
                        try {
                            String input = br.readLine();
                            if (StringUtils.isBlank(input)) {
                                continue;
                            } else if (input.startsWith("@login")) {// 登录
                                input = StringUtils.remove(input, "@login");
                                input = StringUtils.trimToEmpty(input);
                                String username = input;
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.login);
                                cmd.setAddtions(new Object[]{
                                        username
                                });
                                cmd.setAddtions(new Object[]{username});
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (input.startsWith("@logout")) {// 登录
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.logout);
                                cmd.setUid(uid);
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (input.startsWith("@date")) {// 登录
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.datetime);
                                cmd.setUid(uid);
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (input.startsWith("@quit")) {// 登录
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.disconnect);
                                cmd.setUid(uid);
                                client.sendNextAndFlush(new Envelope(cmd)).addListener(new ChannelFutureListener() {
                                    @Override
                                    public void operationComplete(ChannelFuture future) throws Exception {
                                        client.close();
                                    }
                                });
                            } else if (input.startsWith("@username")) {// 修改用户名
                                input = StringUtils.remove(input, "@username");
                                input = StringUtils.trimToEmpty(input);
                                String username = input;
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.username);
                                cmd.setUid(uid);
                                cmd.setAddtions(new Object[]{username});
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (input.startsWith("@@")) { //查找用户
                                // 截取剩下的部分作为用户名
                                input = StringUtils.remove(input, "@@");
                                input = StringUtils.trimToEmpty(input);
                                String username = input;
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.findByUsername);
                                cmd.setUid(uid);
                                cmd.setAddtions(new Object[]{username});
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (input.startsWith("@users")) { //查找用户
                                // 截取剩下的部分作为用户名
                                CommandMsg cmd = new CommandMsg();
                                cmd.setType(CommandMsg.Type.users);
                                cmd.setUid(uid);
                                client.sendNextAndFlush(new Envelope(cmd));
                            } else if (chatWith != null) {
                                String msg = input;
                                ChatMsg chat = new ChatMsg();
                                chat.setFromWhoUID(uid);
                                chat.setToWhoUID(chatWith);
                                chat.setContent(msg);
                                client.sendNextAndFlush(new Envelope(chat));
                            } else { // ignored

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.setName("SYSTEM>IN");
            t.start();
            client.init();
            client.ready();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
