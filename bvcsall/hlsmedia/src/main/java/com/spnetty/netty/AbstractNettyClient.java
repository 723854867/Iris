package com.spnetty.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by User: djyin Date: 2/24/14 Time: 4:00 PM 线程安全（带锁）
 * 可以多个线程同时共享一个连接发送数据
 */
public abstract class AbstractNettyClient<T> extends AbstractUnsafeNettyClient<T> implements NettyClient {

	protected final ReentrantLock lock = new ReentrantLock();

    /**
     * 只输入一个url,其他配置通过get/set方法注入
     * @param url 地址  should be of format tcp://host:port?option='value',option='value'
     * @throws URISyntaxException
     */
	public AbstractNettyClient(String url) throws URISyntaxException {
		super(url);
	}

    /**
     * 使用ip和端口来创建一个客户端
     * @param host ip
     * @param port port
     * @param workers workers
     * @param channelOptions options
     */
	public AbstractNettyClient(String host, int port, int workers, Map<ChannelOption<?>, Object> channelOptions) {
		super(host, port, workers, channelOptions);
	}

    /**
     * 使用url来创建一个客户端
     * @param uri URL should be of format tcp://host:port?option='value',option='value'
     * @param workers workers
     * @param channelOptions options
     * @throws URISyntaxException
     */
	public AbstractNettyClient(String uri, int workers, Map<ChannelOption<?>, Object> channelOptions) throws URISyntaxException {
		super(uri, workers, channelOptions);
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
	 * @param msg
	 *            the msg
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
	 * @param msg
	 *            the notification
	 * @throws InterruptedException
	 *             the interrupted exception
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
	 * @throws InterruptedException
	 *             the interrupted exception
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
	 * @param msg
	 *            the notification
	 * @throws InterruptedException
	 *             the interrupted exception
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
