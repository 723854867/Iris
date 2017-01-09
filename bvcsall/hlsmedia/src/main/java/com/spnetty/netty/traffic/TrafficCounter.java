package com.spnetty.netty.traffic;


import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 主体拷贝自 io.netty.handler.traffic.TrafficCounter
 * 用来计算流量
 * 并将原来用long计数的地方修改成int
 */
public class TrafficCounter {

    /**
     * Default delay between two checks: 1s
     */
    public static final long DEFAULT_CHECK_INTERVAL = 1000;


    /**
     * Current written messages
     */
    private final AtomicInteger currentWrittenMessages = new AtomicInteger();

    /**
     * Current read messages
     */
    private final AtomicInteger currentReadMessages = new AtomicInteger();

    /**
     * Long life written messages
     */
    private final AtomicInteger cumulativeWrittenMessages = new AtomicInteger();

    /**
     * Long life read messages
     */
    private final AtomicInteger cumulativeReadMessages = new AtomicInteger();

    /**
     * Last Time where cumulative messages where reset to zero
     */
    private long lastCumulativeTime;

    /**
     * Last writing bandwidth
     */
    private int lastWriteThroughput;

    /**
     * Last reading bandwidth
     */
    private int lastReadThroughput;

    /**
     * Last Time Check taken
     */
    private final AtomicLong lastTime = new AtomicLong();

    /**
     * Last written messages number during last check interval
     */
    private int lastWrittenMessages;

    /**
     * Last read messages number during last check interval
     */
    private int lastReadMessages;

    /**
     * Delay between two captures
     */
    final AtomicLong checkInterval = new AtomicLong(DEFAULT_CHECK_INTERVAL);

    // default 1 s

    /**
     * Name of this Monitor
     */
    final String name;

    /**
     * The associated TrafficShapingHandler
     */
    private final AbstractTrafficShapingHandler trafficShapingHandler;

    /**
     * Executor that will run the monitor
     */
    private final ScheduledExecutorService executor;
    /**
     * Monitor created once in start()
     */
    private Runnable monitor;
    /**
     * used in stop() to cancel the timer
     */
    private volatile ScheduledFuture<?> scheduledFuture;

    /**
     * Is Monitor active
     */
    final AtomicBoolean monitorActive = new AtomicBoolean();

    /**
     * Class to implement monitoring at fix delay
     */
    private static class TrafficMonitoringTask implements Runnable {
        /**
         * The associated TrafficShapingHandler
         */
        private final AbstractTrafficShapingHandler trafficShapingHandler;

        /**
         * The associated TrafficCounter
         */
        private final TrafficCounter counter;

        /**
         * @param trafficShapingHandler The parent handler to which this task needs to callback to for accounting
         * @param counter               The parent TrafficCounter that we need to reset the statistics for
         */
        protected TrafficMonitoringTask(
                AbstractTrafficShapingHandler trafficShapingHandler,
                TrafficCounter counter) {
            this.trafficShapingHandler = trafficShapingHandler;
            this.counter = counter;
        }

        @Override
        public void run() {
            if (!counter.monitorActive.get()) {
                return;
            }
            long endTime = System.currentTimeMillis();
            counter.resetAccounting(endTime);
            if (trafficShapingHandler != null) {
                trafficShapingHandler.doAccounting(counter);
            }
            counter.scheduledFuture = counter.executor.schedule(this, counter.checkInterval.get(),
                    TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Start the monitoring process
     */
    public void start() {
        synchronized (lastTime) {
            if (monitorActive.get()) {
                return;
            }
            lastTime.set(System.currentTimeMillis());
            if (checkInterval.get() > 0) {
                monitorActive.set(true);
                monitor = new TrafficMonitoringTask(trafficShapingHandler, this);
                scheduledFuture =
                        executor.schedule(monitor, checkInterval.get(), TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Stop the monitoring process
     */
    public void stop() {
        synchronized (lastTime) {
            if (!monitorActive.get()) {
                return;
            }
            monitorActive.set(false);
            resetAccounting(System.currentTimeMillis());
            if (trafficShapingHandler != null) {
                trafficShapingHandler.doAccounting(this);
            }
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    }

    /**
     * Reset the accounting on Read and Write
     *
     * @param newLastTime the millisecond unix timestamp that we should be considered up-to-date for
     */
    void resetAccounting(long newLastTime) {
        synchronized (lastTime) {
            long interval = newLastTime - lastTime.getAndSet(newLastTime);
            if (interval == 0) {
                // nothing to do
                return;
            }
            lastReadMessages = currentReadMessages.getAndSet(0);
            lastWrittenMessages = currentWrittenMessages.getAndSet(0);
            lastReadThroughput = lastReadMessages / (int) interval * 1000;
            // nb byte / checkInterval in ms * 1000 (1s)
            lastWriteThroughput = lastWrittenMessages / (int) interval * 1000;
            // nb byte / checkInterval in ms * 1000 (1s)
        }
    }

    /**
     * Constructor with the {@link AbstractTrafficShapingHandler} that hosts it, the Timer to use, its
     * name, the checkInterval between two computations in millisecond
     *
     * @param trafficShapingHandler the associated AbstractTrafficShapingHandler
     * @param executor              the underlying executor service for scheduling checks
     * @param name                  the name given to this monitor
     * @param checkInterval         the checkInterval in millisecond between two computations
     */
    public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler,
                          ScheduledExecutorService executor, String name, long checkInterval) {
        this.trafficShapingHandler = trafficShapingHandler;
        this.executor = executor;
        this.name = name;
        lastCumulativeTime = System.currentTimeMillis();
        configure(checkInterval);
    }

    /**
     * Change checkInterval between two computations in millisecond
     *
     * @param newcheckInterval The new check interval (in milliseconds)
     */
    public void configure(long newcheckInterval) {
        long newInterval = newcheckInterval / 10 * 10;
        if (checkInterval.get() != newInterval) {
            checkInterval.set(newInterval);
            if (newInterval <= 0) {
                stop();
                // No more active monitoring
                lastTime.set(System.currentTimeMillis());
            } else {
                // Start if necessary
                start();
            }
        }
    }

    /**
     * Computes counters for Read.
     *
     * @param recv the size in messages to read
     */
    void messagesRecvFlowControl(int recv) {
        currentReadMessages.addAndGet(recv);
        cumulativeReadMessages.addAndGet(recv);
    }

    /**
     * Computes counters for Write.
     *
     * @param write the size in messages to write
     */
    void messagesWriteFlowControl(int write) {
        currentWrittenMessages.addAndGet(write);
        cumulativeWrittenMessages.addAndGet(write);
    }

    /**
     * @return the current checkInterval between two computations of traffic counter
     * in millisecond
     */
    public long checkInterval() {
        return checkInterval.get();
    }

    /**
     * @return the Read Throughput in messages/s computes in the last check interval
     */
    public int lastReadThroughput() {
        return lastReadThroughput;
    }

    /**
     * @return the Write Throughput in messages/s computes in the last check interval
     */
    public int lastWriteThroughput() {
        return lastWriteThroughput;
    }

    /**
     * @return the number of messages read during the last check Interval
     */
    public int lastReadmessages() {
        return lastReadMessages;
    }

    /**
     * @return the number of messages written during the last check Interval
     */
    public int lastWrittenmessages() {
        return lastWrittenMessages;
    }

    /**
     * @return the current number of messages read since the last checkInterval
     */
    public int currentReadmessages() {
        return currentReadMessages.get();
    }

    /**
     * @return the current number of messages written since the last check Interval
     */
    public int currentWrittenmessages() {
        return currentWrittenMessages.get();
    }

    /**
     * @return the Time in millisecond of the last check as of System.currentTimeMillis()
     */
    public long lastTime() {
        return lastTime.get();
    }

    /**
     * @return the cumulativeWrittenMessages
     */
    public long cumulativeWrittenmessages() {
        return cumulativeWrittenMessages.get();
    }

    /**
     * @return the cumulativeReadMessages
     */
    public long cumulativeReadmessages() {
        return cumulativeReadMessages.get();
    }

    /**
     * @return the lastCumulativeTime in millisecond as of System.currentTimeMillis()
     * when the cumulative counters were reset to 0.
     */
    public long lastCumulativeTime() {
        return lastCumulativeTime;
    }

    /**
     * Reset both read and written cumulative messages counters and the associated time.
     */
    public void resetCumulativeTime() {
        lastCumulativeTime = System.currentTimeMillis();
        cumulativeReadMessages.set(0);
        cumulativeWrittenMessages.set(0);
    }

    /**
     * @return the name
     */
    public String name() {
        return name;
    }

    /**
     * String information
     */
    @Override
    public String toString() {
        return "Monitor " + name + " Current Speed Read: " +
                (lastReadThroughput >> 10) + " KB/s, Write: " +
                (lastWriteThroughput >> 10) + " KB/s Current Read: " +
                (currentReadMessages.get() >> 10) + " KB Current Write: " +
                (currentWrittenMessages.get() >> 10) + " KB";
    }
}
