package com.spnetty.akka;

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
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by djyin on 14-5-26.
 * 基于Json的socket服务器
 */
public class ChatTcpSocketServer extends TcpSocketServer {
    private static final Logger log = LoggerFactory.getLogger(ChatTcpSocketServer.class);

    final AtomicInteger ctxCounter = new AtomicInteger(0);
    final AtomicLong inboundCounter = new AtomicLong(0);


    @Override
    public ChannelInitializer<SocketChannel> channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();

                p.addLast("logging", new LoggingHandler(LogLevel.WARN));

                p.addLast("sessionCounter", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.deregister(ctx, promise);
                    }

                    @Override
                    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.close(ctx, promise);
                    }

                    @Override
                    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
                        super.connect(ctx, remoteAddress, localAddress, promise);
                        ctxCounter.addAndGet(1);
                    }
                    @Override
                    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.disconnect(ctx, promise);
                        ctxCounter.addAndGet(-1);
                        UsrSessionManager.destory(ctx.channel());
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
                //p.addLast("bytesTrafficShaping", new io.netty.handler.traffic.ChannelTrafficShapingHandler(0, 1000));

                p.addLast("frameEncoder", new LengthFieldPrepender(8));
                p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
                // decoder and encoder
                p.addLast("coder", new FastjsonEnvelopeDecoder());

                // 基于消息数目的,需要放到encoder之后,按照envelope数量进行流控
                p.addLast("msgTrafficShaping", new com.spnetty.netty.traffic.ChannelTrafficShapingHandler(10, 10));
                // 解封envelope
                p.addLast("extractor", new SimpleChannelInboundHandler<Envelope>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, Envelope msg) throws Exception {
                        Object body = msg.getBody();
                        inboundCounter.addAndGet(1);
                        ctx.fireChannelRead(body);
                    }
                });
                p.addLast("handlIn", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelRegistered(ctx);
                    }

                    @Override
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelUnregistered(ctx);
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        super.channelInactive(ctx);
                    }


                });
                p.addLast("handleOut", new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.deregister(ctx, promise);
                    }

                    @Override
                    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.close(ctx, promise);
                    }

                    @Override
                    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
                        super.connect(ctx, remoteAddress, localAddress, promise);
                        ctxCounter.addAndGet(1);
                    }
                    @Override
                    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
                        super.disconnect(ctx, promise);
                        ctxCounter.addAndGet(-1);
                        UsrSessionManager.destory(ctx.channel());
                    }
                });
                // 响应对方心跳
                p.addLast("heartBeatResponder", new SimpleChannelInboundHandler<Heartbeat>() {
                    protected void channelRead0(ChannelHandlerContext ctx, Heartbeat in) throws Exception {
                        Heartbeat hb = in;
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
                });
                // 处理聊天消息
                p.addLast("chatResponseHandler", new SimpleChannelInboundHandler<ChatMsg>() {
                    @Override
                    protected void channelRead0(final ChannelHandlerContext ctx, ChatMsg in) throws Exception {
                        final ChatMsg chatMsg = in;
                        System.out.println(String.format("[TXT] message  %s", chatMsg));
                        UsrSession frowWho = UsrSessionManager.find(ctx.channel());
                        if (frowWho == null) {
                            return;
                        }
                        // 处理转发
                        String toWhoUID = chatMsg.getToWhoUID();
                        UsrSession toWho = UsrSessionManager.find(toWhoUID);
                        if (toWho != null) {
                            String toWhoName = toWho.getUsername();
                            in.setToWhoName(toWhoName);
                            String fromWhoName = frowWho.getUsername();
                            in.setFromWhoName(fromWhoName);
                            ChannelFuture f = toWho.channel.writeAndFlush(new Envelope(in));
                            f.addListener(new GenericFutureListener<ChannelFuture>() {
                                public void operationComplete(final ChannelFuture future) throws InterruptedException {
                                    // IO异常的处理
                                    Throwable e = future.cause(); //java.nio.channels.ClosedChannelException
                                    if (e != null) {
                                        log.error(e.getMessage(), e);
                                        chatMsg.setResult(ChatMsg.ResultType.exception);
                                        ctx.writeAndFlush(new Envelope(chatMsg));
                                    } else {
                                        chatMsg.setResult(ChatMsg.ResultType.ok);
                                        ctx.writeAndFlush(new Envelope(chatMsg));
                                    }
                                }

                            });
                        } else {
                            in.setResult(ChatMsg.ResultType.user_not_available);
                            ctx.writeAndFlush(new Envelope(in));
                        }
                    }
                });
                // 处理命令

                p.addLast("cmdResponseHandler", new SimpleChannelInboundHandler<CommandMsg>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, CommandMsg in) throws Exception {
                        CommandMsg cmd = in;
                        Envelope env = new Envelope(in);
                        switch (cmd.getType()) {
                            case login:
                                String username = (String) cmd.getAddtions()[0];
                                try {
                                    UsrSession session = UsrSessionManager.create(ctx.channel(), username);
                                    in.setResult(CommandMsg.RESULT_OK);
                                    in.setAddtions(new Object[]{session.getUid(), session.getUsername(), session.getProperties()});
                                    ctx.writeAndFlush(env);
                                } catch (UsernameExistException ue) {
                                    in.setResult(CommandMsg.RESULT_FAIL);
                                    in.setContent(ue.toString());
                                    ctx.writeAndFlush(env);
                                }
                                break;
                            case logout:
                                UsrSessionManager.destory(ctx.channel());
                                in.setResult(CommandMsg.RESULT_OK);
                                ctx.writeAndFlush(env);
                                break;
                            case username:
                                username = (String) cmd.getAddtions()[0];
                                try {
                                    UsrSession session = UsrSessionManager.update(ctx.channel(), username);
                                    in.setResult(CommandMsg.RESULT_OK);
                                    in.setAddtions(new Object[]{session.getUsername()});
                                    ctx.writeAndFlush(env);
                                } catch (UsernameExistException ue) {
                                    in.setResult(CommandMsg.RESULT_FAIL);
                                    in.setContent(ue.toString());
                                    ctx.writeAndFlush(env);
                                }
                                break;
                            case datetime:
                                in.setResult(CommandMsg.RESULT_OK);
                                in.setAddtions(new Object[]{new Date(System.currentTimeMillis())});
                                ctx.writeAndFlush(env);
                                break;
                            case disconnect:
                                in.setResult(CommandMsg.RESULT_OK);
                                ctx.writeAndFlush(env).addListener(ChannelFutureListener.CLOSE);
                                break;
                            case findByUsername: //查找用户
                                username = (String) in.getAddtions()[0];
                                UsrSession session = UsrSessionManager.findByUsername(username);
                                if (session != null) {
                                    in.setResult(CommandMsg.RESULT_OK);
                                    in.setAddtions(new Object[]{session.getUid(), session.getUsername()});
                                } else {
                                    in.setResult(CommandMsg.RESULT_NULL);
                                }
                                ctx.writeAndFlush(env);
                                break;
                            case users: //查找全部上线用户
                                Set<String> usernames = UsrSessionManager.listNames();
                                in.setResult(CommandMsg.RESULT_OK);
                                in.setAddtions(new Object[]{usernames});
                                ctx.writeAndFlush(env);
                                break;
                            default:
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
            long lastTime = 0;

            @Override
            public void run() {
                try {
                    long thisIn = inboundCounter.get();
                    long thisTime = System.currentTimeMillis();
                    long time = thisTime - lastTime;
                    System.out.println(String.format("[stat] connections: %d, msgs of inbound %d ", ctxCounter.get(), (thisIn - lastIn) * 1000 / time));
                    lastIn = thisIn;
                    lastTime = thisTime;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(task, 0, 30, TimeUnit.SECONDS);
        super.run();
    }

}

// 一个设备只允许一个账户登录一次
class UsrSession {
    String uid; // id
    String username; // 用户名
    Channel channel; // 网络接口信息
    String token; // 认证token
    Date createAt; // session 创建时间
    String[] properties;

    /**
     * @param uid      用户id
     * @param username 用户名
     */
    UsrSession(Channel channel, String uid, String username, String... properties) {
        this.channel = channel;
        this.uid = uid;
        this.username = username;
        this.createAt = new Date(System.currentTimeMillis());
        this.setProperties(properties);
    }

    @Override
    public String toString() {
        return "Session{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public boolean match(UsrSession o) {
        UsrSession usrSession = o;
        if (uid != null ? !uid.equals(usrSession.uid) : usrSession.uid != null) return false;
        if (username != null ? !username.equals(usrSession.username) : usrSession.username != null) return false;
        return true;
    }

    public boolean match(Channel channel, Long uid) {
        if (channel != null ? !channel.equals(this.channel) : this.channel != null) return false;
        if (uid != null ? !uid.equals(this.uid) : this.uid != null) return false;
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsrSession usrSession = (UsrSession) o;

        if (createAt != null ? !createAt.equals(usrSession.createAt) : usrSession.createAt != null) return false;
        if (token != null ? !token.equals(usrSession.token) : usrSession.token != null) return false;
        if (uid != null ? !uid.equals(usrSession.uid) : usrSession.uid != null) return false;
        if (username != null ? !username.equals(usrSession.username) : usrSession.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uid != null ? uid.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        return result;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateAt() {
        return createAt;
    }

}

class UsrSessionManager {

    private static final AttributeKey<UsrSession> ATTRIBUTE_KEY_SESSION = AttributeKey.valueOf(
            UsrSessionManager.class.getName() + ".SESSION");
    // 简单模拟用户session
    // 用户名-->用户id的反查列表
    static final Map<String, String> usernameIds = new HashMap<String, String>();
    // 已经连接上的用户channel
    // 用户id-->
    static final Map<String, Channel> connectedUsrChannels = new HashMap<String, Channel>();

    // 失效这个用户之前的会话
    public static void _fc(Channel ctx) {

    }

    public static UsrSession create(Channel ctx, String username, String... properties) throws UsernameExistException {

        // 如果当前username存在,强制登入
        String eUserId = usernameIds.get(username);
        if (eUserId != null) {
            // 通知
            SystemMsg notice = new SystemMsg();
            notice.setToWhoUID(eUserId);
            InetSocketAddress addr = (InetSocketAddress) ctx.remoteAddress();
            String ip = addr.getAddress().getHostAddress();
            notice.setTitle("WARNING");
            notice.setContent(String.format("Your account has login from another ip: %s", ip));
            Envelope env = new Envelope(notice);
            // 关闭连接,这样即使客户端自动登录了,也是无会话状态
            Channel c = connectedUsrChannels.get(eUserId);
            c.writeAndFlush(env).addListener(ChannelFutureListener.CLOSE);
        }
        // 销毁当前会话
        destory(ctx);
        // 创建新会话
        String uid = UUID.randomUUID().toString();
        UsrSession usrSession = new UsrSession(ctx, uid, username, properties);
        connectedUsrChannels.put(uid, ctx);
        usernameIds.put(username, uid);
        ctx.attr(ATTRIBUTE_KEY_SESSION).set(usrSession);
        return usrSession;
    }

    /**
     * @param ctx
     * @param username
     * @param properties
     * @return
     * @throws UsernameExistException
     * @throws UsrSessionNotExistException
     */
    public static UsrSession update(Channel ctx, String username, String... properties) throws UsernameExistException, UsrSessionNotExistException {

        // 先检查当前连接是否已有会话
        Attribute<UsrSession> att = ctx.attr(ATTRIBUTE_KEY_SESSION);
        UsrSession usrSession = null;
        if (att != null) {
            usrSession = att.get();
        }
        // create new one
        if (usrSession == null) {
            // 创建新会话
            create(ctx, username, properties);
        } else {
            // update session
            usrSession = _update(usrSession, username, properties);
        }
        return usrSession;
    }


    public static UsrSession _update(UsrSession o, String username, String... properties) throws UsernameExistException {
        String eUserId = usernameIds.get(username);
        if (eUserId != null) {
            throw new UsernameExistException();
        }
        usernameIds.remove(o.getUsername());
        usernameIds.put(username, o.getUid());
        o.setUsername(username);
        o.setProperties(properties);
        return o;
    }

    // 关闭会话,但不关闭连接
    public static void _destory(UsrSession o) {
        o.channel = null;
        o.username = null;
        o.token = null;
        o.uid = null;
    }

    // 关闭会话,但不关闭连接
    public static void destory(UsrSession o) {
        o.channel.attr(ATTRIBUTE_KEY_SESSION).remove();
        connectedUsrChannels.remove(o.getUid());
        usernameIds.remove(o.getUid());
        _destory(o);
    }

    // 关闭会话,但不关闭连接
    public static void destory(Channel ctx) {
        // 先检查当前连接是否已有会话
        Attribute<UsrSession> att = ctx.attr(ATTRIBUTE_KEY_SESSION);
        if (att != null) {
            UsrSession usrSession = att.get();
            if (usrSession != null) {
                destory(usrSession);
            }
        }

    }

    // 通过id查找用户
    public static UsrSession find(Channel ctx) {
        return ctx.attr(ATTRIBUTE_KEY_SESSION).get();
    }

    // 通过id查找用户
    public static UsrSession find(String uid) {
        Channel chn = connectedUsrChannels.get(uid);
        return chn != null ? chn.attr(ATTRIBUTE_KEY_SESSION).get() : null;
    }

    // 通过用户名查找用户
    public static UsrSession findByUsername(String usename) {
        String uid = usernameIds.get(usename);
        if (uid == null) return null;
        Channel chn = connectedUsrChannels.get(uid);
        return chn != null ? chn.attr(ATTRIBUTE_KEY_SESSION).get() : null;
    }

    // 列出全部用户
    public static Set<String> listNames() {
        Set<String> usernames = usernameIds.keySet();
        return usernames;
    }

    // 检查用户名是否存在
    public static boolean exists(String usename) {
        String uid = usernameIds.get(usename);
        if (uid == null) return false;
        Channel chn = connectedUsrChannels.get(uid);
        return chn != null;
    }

}

class UsrSessionException extends Exception {
}

class UsrSessionNotExistException extends UsrSessionException {
}

class UsernameExistException extends UsrSessionException {
}
