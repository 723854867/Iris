package com.spnetty.netty.traffic;

import io.netty.channel.*;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.concurrent.TimeUnit;

/**
 * 1. 修改bytes/s的计量方式为messages/s
 * 2. 将原来内部的long型计数器切换成int
 * 3. 通过修改autoread方式,减少堆内存消耗
 * <p/>
 * AbstractTrafficShapingHandler allows to limit the global bandwidth
 * (see {@link io.netty.handler.traffic.GlobalTrafficShapingHandler}) or per session
 * bandwidth (see {@link io.netty.handler.traffic.ChannelTrafficShapingHandler}), as traffic shaping.
 * It allows you to implement an almost real time monitoring of the bandwidth using
 * the monitors from {@link io.netty.handler.traffic.TrafficCounter} that will call back every checkInterval
 * the method doAccounting of this handler.<br>
 * <br>
 * <p/>
 * If you want for any particular reasons to stop the monitoring (accounting) or to change
 * the read/write limit or the check interval, several methods allow that for you:<br>
 * <ul>
 * <li><tt>configure</tt> allows you to change read or write limits, or the checkInterval</li>
 * <li><tt>getTrafficCounter</tt> allows you to have access to the TrafficCounter and so to stop
 * or start the monitoring, to change the checkInterval directly, or to have access to its values.</li>
 * </ul>
 */
public abstract class AbstractTrafficShapingHandler extends ChannelDuplexHandler {

    /**
     * Default delay between two checks: 1s
     */
    public static final long DEFAULT_CHECK_INTERVAL = 1000;

    /**
     * Default minimal time to wait
     */
    private static final long MINIMAL_WAIT = 10;

    /**
     * Traffic Counter
     */
    protected TrafficCounter trafficCounter;

    /**
     * Limit in messages/s to apply to write
     */
    private int writeLimit;

    /**
     * Limit in messages/s to apply to read
     */
    private int readLimit;

    /**
     * Delay between two performance snapshots
     */
    protected long checkInterval = DEFAULT_CHECK_INTERVAL; // default 1 s

    private static final AttributeKey<Boolean> READ_SUSPENDED = AttributeKey.valueOf(
            AbstractTrafficShapingHandler.class.getName() + ".READ_SUSPENDED");
    private static final AttributeKey<Runnable> REOPEN_TASK = AttributeKey.valueOf(
            AbstractTrafficShapingHandler.class.getName() + ".REOPEN_TASK");

    /**
     * @param newTrafficCounter the TrafficCounter to set
     */
    void setTrafficCounter(TrafficCounter newTrafficCounter) {
        trafficCounter = newTrafficCounter;
    }

    /**
     * @param writeLimit    0 or a limit in messages/s
     * @param readLimit     0 or a limit in messages/s
     * @param checkInterval The delay between two computations of performances for
     *                      channels or 0 if no stats are to be computed
     */
    protected AbstractTrafficShapingHandler(int writeLimit, int readLimit,
                                            long checkInterval) {
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
        this.checkInterval = checkInterval;
    }

    /**
     * Constructor using default Check Interval
     *
     * @param writeLimit 0 or a limit in messages/s
     * @param readLimit  0 or a limit in messages/s
     */
    protected AbstractTrafficShapingHandler(int writeLimit, int readLimit) {
        this(writeLimit, readLimit, DEFAULT_CHECK_INTERVAL);
    }

    /**
     * Constructor using NO LIMIT and default Check Interval
     */
    protected AbstractTrafficShapingHandler() {
        this(0, 0, DEFAULT_CHECK_INTERVAL);
    }

    /**
     * Constructor using NO LIMIT
     *
     * @param checkInterval The delay between two computations of performances for
     *                      channels or 0 if no stats are to be computed
     */
    protected AbstractTrafficShapingHandler(long checkInterval) {
        this(0, 0, checkInterval);
    }

    /**
     * Change the underlying limitations and check interval.
     *
     * @param newWriteLimit    The new write limit (in messages)
     * @param newReadLimit     The new read limit (in messages)
     * @param newCheckInterval The new check interval (in milliseconds)
     */
    public void configure(int newWriteLimit, int newReadLimit,
                          long newCheckInterval) {
        configure(newWriteLimit, newReadLimit);
        configure(newCheckInterval);
    }

    /**
     * Change the underlying limitations.
     *
     * @param newWriteLimit The new write limit (in messages)
     * @param newReadLimit  The new read limit (in messages)
     */
    public void configure(int newWriteLimit, int newReadLimit) {
        writeLimit = newWriteLimit;
        readLimit = newReadLimit;
        if (trafficCounter != null) {
            trafficCounter.resetAccounting(System.currentTimeMillis() + 1);
        }
    }

    /**
     * Change the check interval.
     *
     * @param newCheckInterval The new check interval (in milliseconds)
     */
    public void configure(long newCheckInterval) {
        checkInterval = newCheckInterval;
        if (trafficCounter != null) {
            trafficCounter.configure(checkInterval);
        }
    }

    /**
     * Called each time the accounting is computed from the TrafficCounters.
     * This method could be used for instance to implement almost real time accounting.
     *
     * @param counter the TrafficCounter that computes its performance
     */
    @SuppressWarnings("unused")
    protected void doAccounting(TrafficCounter counter) {
        // NOOP by default
    }

    /**
     * Class to implement setReadable at fix time
     */
    private static final class ReopenReadTimerTask implements Runnable {
        final ChannelHandlerContext ctx;

        ReopenReadTimerTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            ctx.attr(READ_SUSPENDED).set(false);
            if (!ctx.channel().config().isAutoRead()) {
                ctx.channel().config().setAutoRead(true);
            }
            ctx.read();

        }
    }

    /**
     * @return the time that should be necessary to wait to respect limit. Can be negative time
     */
    private static long getTimeToWait(int limit, int messages, long lastTime, long curtime) {
        long interval = curtime - lastTime;
        if (interval <= 0) {
            // Time is too short, so just lets continue
            return 0;
        }
        // 修正计算方法
        // return ((messages - (interval / 1000) * limit) / limit) * 1000;
        // 优化
        return messages * 1000 / limit - interval;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        int size = calculateSize(msg);
        long curtime = System.currentTimeMillis();

        if (trafficCounter != null) {
            trafficCounter.messagesRecvFlowControl(size);
            if (readLimit == 0) {
                // no action
                ctx.fireChannelRead(msg);
                return;
            }

            // compute the number of ms to wait before reopening the channel
            long wait = getTimeToWait(readLimit,
                    trafficCounter.currentReadmessages(),
                    trafficCounter.lastTime(), curtime);
            if (wait >= MINIMAL_WAIT) { // At least 10ms seems a minimal
                // time in order to
                // try to limit the traffic
                if (!isSuspended(ctx)) {
                    // 1.默认的方式,通过read空转来实现
                    ctx.attr(READ_SUSPENDED).set(true);
                    // 2.强力实现,通过将channel从eventloop中反注册掉实现,缺点是不好实现读写双向的限制,而且会导致数据破碎
                    //ctx.attr(CHANNEL_DEREGISTERED).set(true);
                    //ctx.channel().deregister();
                    // 3.文艺型实现,通过设置autoread设置实现.增加这个配置,减少服务器的heap内存的消耗
                    ctx.channel().config().setAutoRead(false);

                    // Create a Runnable to reactive the read if needed. If one was create before it will just be
                    // reused to limit object creation
                    Attribute<Runnable> attr = ctx.attr(REOPEN_TASK);
                    Runnable reopenTask = attr.get();
                    if (reopenTask == null) {
                        reopenTask = new ReopenReadTimerTask(ctx);
                        attr.set(reopenTask);
                    }
                    ctx.executor().schedule(reopenTask, wait,
                            TimeUnit.MILLISECONDS);
                } else {
                    // Create a Runnable to update the next handler in the chain. If one was create before it will
                    // just be reused to limit object creation
                    Runnable bufferUpdateTask = new Runnable() {
                        @Override
                        public void run() {
                            ctx.fireChannelRead(msg);
                        }
                    };
                    ctx.executor().schedule(bufferUpdateTask, wait, TimeUnit.MILLISECONDS);
                    return;
                }
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void read(ChannelHandlerContext ctx) {
        if (!isSuspended(ctx)) {
            ctx.read();
        }
    }

    private static boolean isSuspended(ChannelHandlerContext ctx) {

        Boolean suspended = ctx.attr(READ_SUSPENDED).get();
        if (suspended == null || Boolean.FALSE.equals(suspended)) {
            return false;
        }

        ctx.channel().config().setAutoRead(true);
        return true;
    }

    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise)
            throws Exception {
        long curtime = System.currentTimeMillis();
        int size = calculateSize(msg);

        if (size > -1 && trafficCounter != null) {
            trafficCounter.messagesWriteFlowControl(size);
            if (writeLimit == 0) {
                ctx.write(msg, promise);
                return;
            }
            // compute the number of ms to wait before continue with the
            // channel
            long wait = getTimeToWait(writeLimit,
                    trafficCounter.currentWrittenmessages(),
                    trafficCounter.lastTime(), curtime);
            if (wait >= MINIMAL_WAIT) {
                ctx.executor().schedule(new Runnable() {
                    @Override
                    public void run() {
                        ctx.write(msg, promise);
                    }
                }, wait, TimeUnit.MILLISECONDS);
                return;
            }
        }
        ctx.write(msg, promise);
    }

    /**
     * @return the current TrafficCounter (if
     * channel is still connected)
     */
    public TrafficCounter trafficCounter() {
        return trafficCounter;
    }

    @Override
    public String toString() {
        return "TrafficShaping with Write Limit: " + writeLimit +
                " Read Limit: " + readLimit + " and Counter: " +
                (trafficCounter != null ? trafficCounter.toString() : "none");
    }

    /**
     * Calculate the size of the given {@link Object}.
     *
     * @param msg the msg for which the size should be calculated
     * @return size     the size of the msg or {@code -1} if unknown.
     */
    protected int calculateSize(Object msg) {
        if (msg != null) {
            return 1;
        }
        return -1;
    }
}
