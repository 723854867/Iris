package com.spnetty.netty;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by
 * User: djyin
 * Date: 2/25/14
 * Time: 1:36 PM
 * 一个通用的压力测试端口，基本逻辑是：
 * 启动一个 workers 容量的执行队列,执行 tasks 次测试，每次执行 burstsPerTask * pushsPerBurst 次。
 */
public abstract class AbstractBenchmark {

    // 不使用log框架，为了保证输出的完整
    public static void logMe(String format, Object... args) {
        System.out.println(Thread.currentThread() + String.format(format, args));
    }

    // 发送的同时并发数量
    public int workers = 10;
    // 一共需要完成多少次task
    public int tasks = 20;
    // 每次task要执行多少次burst
    public int burstsPerTask = 100;
    // 每个burst要发送多少msg
    public int pushsPerBurst = 100;
    // 最长执行时间
    public long maxElapSecs = 0;//elap_secs

    // 全局执行完成标识
    private final AtomicBoolean finshed = new AtomicBoolean(false);

    // 统计计数器
    private final AtomicInteger cLeftTasks = new AtomicInteger(0);
    private final AtomicInteger cFinshedTasks = new AtomicInteger(0);
    private final AtomicLong cSuccessPushs = new AtomicLong(0);
    private final AtomicLong cFailPushs = new AtomicLong(0);
    private final AtomicLong cTotalPushs = new AtomicLong(0);
    // 计算消息量
    private long cPushs = 0L;

    // 统计计时器
    private final AtomicLong timing = new AtomicLong(0);


    protected AbstractBenchmark(int workers, int tasks, int burstsPerTask, int pushsPerBurst, long maxElapSecs) {
        this.workers = workers;
        this.tasks = tasks;
        this.pushsPerBurst = pushsPerBurst;
        this.burstsPerTask = burstsPerTask;
        this.maxElapSecs = maxElapSecs;
        this.cLeftTasks.addAndGet(this.tasks);
        this.cPushs = (long) tasks * (long) burstsPerTask * (long) pushsPerBurst;
    }

    private AbstractBenchmark() {
        // 空构造函数
    }

    /**
     * Create burstsPerTask tasks.
     *
     * @return the list
     */
    public List<PushTask> createBurstTasks() {
        List<PushTask> tasks = new ArrayList<PushTask>(this.tasks);
        for (int i = 0; i < this.tasks; i++) {
            tasks.add(createBurstTask());
        }
        return tasks;
    }

    // task的序列号
    private AtomicInteger taskSeq = new AtomicInteger(0);

    public PushTask createBurstTask() {
        Integer seq = taskSeq.addAndGet(1);
        if (seq > this.tasks) {
            return null;
        }
        PushTask task = new PushTask(this, seq);
        return task;
    }

    /**
     * 执行测试
     * 通过CompletionService实现
     */
    public void complete() {

        final ExecutorService executor = Executors.newFixedThreadPool(this.workers);
        final CompletionService<PushResult> compService = new ExecutorCompletionService<PushResult>(executor);
        final List<PushTask> pushTasks = createBurstTasks();
        // 启动打印线程
        Thread printer = new Thread(new PrintTask());
        printer.start();
        // 主进程不断循环添加任务
        try {
            // 提交任务
            for (PushTask task : pushTasks) {
                compService.submit(task);
            }
            for (int i = 0; i < tasks; i++) {
                try {
                    Future<PushResult> future = compService.take();
                    if (future != null) {
                        PushResult result = future.get();
                        cFinshedTasks.addAndGet(1);
                        logMe("Benchmark Task : %d,\t total : %,d,\t success : %,d,\t consume : %,.2f ms %n",
                                result.SEQ, result.TOTAL, result.SUCCESS, result.TIMING);

                    }
                } catch (InterruptedException e) {
                    // ignored
                } catch (ExecutionException e) {
                    logMe("fail to exec:", e);
                }
            }
            // 设置成功状态
            finshed.getAndSet(true);

        } finally {
            executor.shutdown(); //always reclaim resources
        }

    }


    /**
     * 并行执行
     * 直接通过fixedThreadPool来实现
     */
    public void execute() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(this.workers);
        Collection<Callable<PushResult>> tasks = new ArrayList<Callable<PushResult>>(this.tasks);
        for (PushTask task : createBurstTasks()) {
            tasks.add(task);
        }
        // 启动打印线程
        Thread printer = new Thread(new PrintTask());
        printer.start();
        try {
            List<Future<PushResult>> futures = executor.invokeAll(tasks);
            for (Future<PushResult> future : futures) {
                try {
                    PushResult result = future.get();
                    cFinshedTasks.addAndGet(1);
                    logMe("Benchmark Task : %d,\t total : %,d,\t success : %,d,\t consume : %,.2f ms %n",
                            result.SEQ, result.TOTAL, result.SUCCESS, result.TIMING);
                } catch (Exception e) {

                }
            }
            // 设置成功状态
            finshed.getAndSet(true);
        } finally {
            executor.shutdown(); //always reclaim resources
        }

    }


    /**
     * 测试结果，task执行结果或者一次函数的执行结果
     */
    public class PushResult {

        public Integer SEQ;
        public Integer SUCCESS = 0;
        public Long TIMING = 0L;
        public Integer TOTAL = 0;

        @Override
        public String toString() {
            return "PushResult SEQ:" + ",TOTAL:" + TOTAL + ",SUCCESS:" + SUCCESS + ", TIMING:" + TIMING;
        }

    }

    class PushTask implements Callable<PushResult> {

        final AbstractBenchmark mother;
        Integer seq = null;

        PushTask(AbstractBenchmark mother, Integer seq) {
            this.mother = mother;
            this.seq = seq;
        }

        public PushResult call() throws Exception {
            PushResult summary = new PushResult();
            summary.SEQ = this.seq;
            long st = System.currentTimeMillis();
            // 循环执行多次
            for (int j = 0; j < burstsPerTask; j++) {
                for (int i = 0; i < pushsPerBurst; i++) {
                    PushResult result = mother.pushMsg();
                    summary.TIMING = summary.TIMING + result.TIMING;
                    summary.TOTAL = summary.TOTAL + result.TOTAL;
                    summary.SUCCESS = summary.SUCCESS + result.SUCCESS;
                    // 更新全局统计数
                    cSuccessPushs.addAndGet(result.SUCCESS);
                    cFailPushs.addAndGet(result.TOTAL - result.SUCCESS);
                    cTotalPushs.addAndGet(result.TOTAL);
                    timing.addAndGet(result.TIMING);
                }
            }

            long et = System.currentTimeMillis();
            long consumed = et - st;
            if (summary.TIMING <= 0) {
                summary.TIMING = consumed;
            }
            return summary;
        }

    }

    /**
     * 子类必须实现的接口，用来执行一次操作，比如一次数据插入，一次数据查询
     *
     * @return
     * @throws Exception
     */
    public abstract PushResult pushMsg() throws Exception;

    /**
     * 打印线程
     */
    class PrintTask implements Runnable {

        @Override
        public void run() {
            // 运行开始时间
            long t0 = System.currentTimeMillis();
            // 上一次运行时间
            long lastMs = System.currentTimeMillis();
            long count = 0;
            long lastTotal = 0;
            long lastSucc = 0;
            long lastFail = 0;

            while (true) {
                // 每隔10次打印一次头信息
                if (count % 5 == 0) {
                    logMe("tps\t : total_cnt\t : succ_cnt\t : fail_cnt\t : elap_secs\t : succ_rate %n");
                }

                // 记录时间
                long now = System.currentTimeMillis();
                long elapsed = now - t0;
                // 计算间隔，因为依靠Thread.sleep()的定时器不准确
                long thisIntervalMs = now - lastMs;
                // 计算tps
                long thisTotal = cTotalPushs.get();
                long thisSucc = cSuccessPushs.get();
                long thisFail = cFailPushs.get();

                double tps = ((double) thisTotal - (double) lastTotal) * 1000 / (double) thisIntervalMs;
                // 计算成功率
                double sRate = ((double) thisSucc - (double) lastSucc) / ((double) thisTotal - (double) lastTotal) * 100;

                logMe("%,.2f\t : %,d/%,d \t : %,d \t :%,d \t %,.2f \t %,.2f %n",
                        tps, thisTotal - lastTotal, cPushs, thisSucc - lastSucc, thisFail - lastFail, elapsed / 1000l, sRate);

                try {
                    if (finshed.get()) {
                        break;
                    }
                    if (maxElapSecs > 0 && elapsed > ((long) maxElapSecs) * 1000) {
                        break;
                    }
                    // 每隔 1 秒打印一次
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    //ignored
                }
                lastTotal = thisTotal;
                lastSucc = thisSucc;
                lastFail = thisFail;
                lastMs = now;
                count++;
            }

        }
    }


}
