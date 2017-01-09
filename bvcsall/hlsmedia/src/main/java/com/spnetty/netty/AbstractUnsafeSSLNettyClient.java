package com.spnetty.netty;

import io.netty.channel.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.util.Map;

/**
 * Created by
 * User: djyin
 * Date: 2/24/14
 * Time: 4:00 PM
 * 特征和AbstractNettyClient一致，但是不保证发送过程的线程安全，即不可以几个线程同时调用一个client实例发送数据。
 * 适用于连接吃的情况，每个线程在独享一个NettyClient
 */
public abstract class AbstractUnsafeSSLNettyClient<T>
        extends AbstractUnsafeNettyClient<T> implements NettyClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractUnsafeSSLNettyClient.class);

    private final KeyStore ks;

    private final char[] ksPwd;

    /**
     * Constructs a new APNs client thread. The thread connects to the APNs gateway in the given {@code PushManager}'s
     * environment and reads notifications from the {@code PushManager}'s queue.
     */
    @SuppressWarnings("unchecked")
    public AbstractUnsafeSSLNettyClient(final String host, final int port, final int workers,
                                        Map<ChannelOption<?>, Object> channelOptions, KeyStore ks, char[] ksPwd) {
        super(host, port, workers, channelOptions);
        this.ks = ks;
        this.ksPwd = ksPwd;
    }



    // 尝试重连
    // 覆盖父类的方法，增加SSL握手的部分
    // 特点是和父类一样，提供一个阻塞的建立连接的函数
    protected boolean _connectOrContinueConnecting() {
        if (super._connectOrContinueConnecting()) {
            final SslHandler sslHandler = this.channel.pipeline().get(SslHandler.class);
            if (sslHandler == null) {
                throw new IllegalStateException(
                        String.format("%s failed to get SSL handler for missing SslHanlder.",
                                this.getName()));
            }
            Future<Channel> handshakeFuture;
            if (sslHandler != null) {
                handshakeFuture = sslHandler.handshakeFuture();
            } else {
                log.error(String.format("%s failed to get SSL handler and could not wait for a TLS handshake.", this.getName()));
                if (this.channel != null && this.channel.isOpen()) {
                    this.channel.close();
                }
                this.channel = null;
                return false;
            }
            try {
                handshakeFuture.syncUninterruptibly();
            } catch (Exception e) {
                // ignored all exception
                log.warn("fail to ssl handshake", e);
            }
            if (handshakeFuture.isSuccess()) {
                log.debug(String.format("%s successfully completed TLS handshake.", this.getName()));
                return true;
            } else {
                log.error(String.format("%s failed to complete TLS handshake with APNs gateway.", this.getName()),
                        handshakeFuture.cause());
                if (this.channel != null && this.channel.isOpen()) {
                    channel.close();
                }
                this.channel = null;
                return false;
            }
        }
        return false;
    }


}
