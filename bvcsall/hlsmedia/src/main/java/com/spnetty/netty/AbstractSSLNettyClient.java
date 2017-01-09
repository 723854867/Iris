package com.spnetty.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;

import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by
 * User: djyin
 * Date: 2/24/14
 * Time: 4:00 PM
 * 线程安全（带锁）
 * 可以多个线程同时共享一个连接发送数据
 */
public abstract class AbstractSSLNettyClient<T> extends AbstractUnsafeSSLNettyClient<T> implements NettyClient {


    protected final ReentrantLock lock = new ReentrantLock();

    /**
     * Constructs a new APNs client thread. The thread connects to the APNs gateway in the given {@code PushManager}'s
     * environment and reads notifications from the {@code PushManager}'s queue.
     *
     * @param host
     * @param port
     * @param workers
     * @param readyTimeoutMillis
     * @param channelOptions
     * @param keyStore
     * @param keyStorePassword
     */
    public AbstractSSLNettyClient(String host, int port, int workers, Map<ChannelOption<?>, Object> channelOptions, KeyStore keyStore, char[] keyStorePassword) {
        super(host, port, workers, channelOptions, keyStore, keyStorePassword);
    }


    /**
     * 自检，达到正常能发送数据的状态，退出状态或者超时
     */
    public boolean ready() {
        boolean acq = false;
        try {
            if (!lock.isLocked()) {
                lock.lock();
                acq = true;
            }
            return super.ready();
        } finally {
            if (acq) {
                lock.unlock();
            }
        }
    }

    /**
     * 同步发送的方式，只有当数据刷到socketa缓冲区后，函数才返回
     *
     * @param msg the msg
     */
    @Override
    public ChannelFuture sendNextSync(final T msg) {
        final ReentrantLock lock = this.lock;
        try {
            lock.lock();
            return super.sendNextSync(msg);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送消息
     *
     * @param msg the notification
     * @throws InterruptedException the interrupted exception
     */
    @Override
    public ChannelFuture sendNext(final T msg) {
        final ReentrantLock lock = this.lock;
        try {
            lock.lock();
            return super.sendNext(msg);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将数据从nio的buff刷到socket的buff中
     *
     * @throws InterruptedException the interrupted exception
     */
    @Override
    public void flush() {
        final ReentrantLock lock = this.lock;
        try {
            lock.lock();
            super.flush();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送消息，注意这个方法不会使用发送队列保存发送的数据。
     *
     * @param msg the notification
     * @throws InterruptedException the interrupted exception
     */
    @Override
    public ChannelFuture sendNextAndFlush(final T msg) {
        final ReentrantLock lock = this.lock;
        try {
            lock.lock();
            return super.sendNextAndFlush(msg);
        } finally {
            lock.unlock();
        }
    }


}
