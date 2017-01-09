package com.spnetty.netty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 参考 Disruptor 实现的一个回环缓存<br/>
 * 1. 增加了默认的long型id
 *
 *
 * @param <E>
 */
public class RingBuffer<E> {

    public static final Long MAX_SEQ = Long.MAX_VALUE;
    public static final Long MIN_SEQ = 0L;

    // 最大容量
    private final int capacity;
    // 默认的序列号生成器
    protected final AtomicLong buffSeqNo;
    protected final Long maxSeq;
    protected final Long minSeq;
    // 存放
    private final Buffered<E>[] elements;

    /**
     * @param capacity buff的容量
     */
    public RingBuffer(int capacity) {
        this(capacity, MIN_SEQ, MIN_SEQ, MAX_SEQ);
    }

    /**
     * @param capacity buff的容量
     * @param initSeq  序列的起始数 及序列最小值
     * @param maxSeq   序列最大值
     */
    public RingBuffer(int capacity, long initSeq, long maxSeq) {
        this(capacity, initSeq, maxSeq, initSeq);
    }

    /**
     * @param capacity buff的容量
     * @param initSeq  序列的起始数
     * @param minSeq   序列最小值
     * @param maxSeq   序列最大值
     */
    public RingBuffer(int capacity, long initSeq, long minSeq, long maxSeq) {
        if (capacity < 0 || capacity > Long.MAX_VALUE / 4) {
            throw new IllegalArgumentException(
                    String.format("RingBuffer's capacity can't be larger than %d or less than 0, request capacity is %d",
                            Long.MAX_VALUE / 4, capacity));
        }
        if (initSeq < 0) {
            throw new IllegalArgumentException(
                    String.format("RingBuffer's initSeq can't be less than 0, request capacity is %d", initSeq));
        }
        this.capacity = capacity;
        this.maxSeq = maxSeq;
        this.minSeq = minSeq;
        elements = new Buffered[capacity];
        buffSeqNo = new AtomicLong(initSeq);
    }

    /**
     * 存放
     *
     * @param obj
     * @return 存放元素在buff中的序列号
     */
    public long put(E obj) {
        Long seq = buffSeqNo.getAndAdd(1L);
        if (seq < 0) { // 越界了
            seq = minSeq;
        }
        if (seq == maxSeq) { // 越界了
            seq = minSeq;
        }
        int w = (int) (seq % capacity);
        elements[w] = new Buffered<E>(seq, obj);
        return seq;
    }

    /**
     * 读取
     *
     * @param seq
     * @return
     */
    public E get(long seq) {
        int w = (int) (seq % capacity);
        Buffered<E> e = elements[w];
        if (e.key == seq) {
            return e.value;
        }
        return null;
    }

    /**
     * 读取全部,按照seq的顺序
     *
     * @return
     */
    public List<E> get() {
        List<E> result = new ArrayList<E>();
        Long seq = this.buffSeqNo.get() - 1;
        int w = (int) (seq % capacity) + 1;
        for (int i = 0; i < capacity; i++) {
            int pos = w + i;
            if (pos > capacity - 1) {
                pos = pos - capacity;
            }
            Buffered<E> e = elements[pos];
            result.add(e.value);
        }
        return result;
    }
    /**
     * 读取某个序列号后,全部的元素,!!不!!包括这个序列号的元素
     * 返回值是有序的,按照seq开始的顺序
     * @param seq
     * @return
     */
    public List<E> tryNext(long seq) {
        if (seq >= this.buffSeqNo.get()) {//错误 输入
            return new ArrayList<E>();
        }
        int w = (int) (seq % capacity);
        if (elements[w].key != seq) {
            // 元素已经被覆盖了一周(capacity)以上,直接取出全部元素
            return get();
        }
        List<E> result = new ArrayList<E>();
        for (int i = 1; i < capacity; i++) {
            int pos = w + i;
            if (pos > capacity - 1) {
                pos = pos - capacity;
            }
            Buffered<E> e = elements[pos];
            if (e == null) { //元素为空,特殊情况.buff还从未充满过
                break;
            }
            if (e.key < seq) {
                if (e.key < capacity && seq > capacity) { //特殊情况,插入buff的数据过多,seq已经超出Long的范围,自动重置而导致.
                    // ignored
                } else {
                    break;
                }
            } else {
                if (seq < capacity && e.key > capacity) { //特殊情况,插入buff的数据过多,seq已经超出Long的范围,自动重置而导致.
                    break;
                } else {
                    // ignored
                }
            }
            result.add(e.value);
        }
        return result;
    }
    /**
     * 读取某个序列号后,全部的元素,包括这个序列号的元素
     * 返回值是有序的,按照seq开始的顺序
     * @param seq
     * @return
     */
    public List<E> withNext(long seq) {
        if (seq >= this.buffSeqNo.get()) {//错误 输入
            return new ArrayList<E>();
        }
        int w = (int) (seq % capacity);
        if (elements[w].key != seq) {
            // 元素已经被覆盖了一周(capacity)以上,直接取出全部元素
            return get();
        }
        List<E> result = new ArrayList<E>();
        result.add(elements[w].value);// 将这个seq对应的元素加入
        for (int i = 1; i < capacity; i++) {
            int pos = w + i;
            if (pos > capacity - 1) {
                pos = pos - capacity;
            }
            Buffered<E> e = elements[pos];
            if (e == null) { //元素为空,特殊情况.buff还从未充满过
                break;
            }
            if (e.key < seq) {
                if (e.key < capacity && seq > capacity) { //特殊情况,插入buff的数据过多,seq已经超出Long的范围,自动重置而导致.
                    // ignored
                } else {
                    break;
                }
            } else {
                if (seq < capacity && e.key > capacity) { //特殊情况,插入buff的数据过多,seq已经超出Long的范围,自动重置而导致.
                    break;
                } else {
                    // ignored
                }
            }
            result.add(e.value);
        }
        return result;
    }

    public static void main(String[] args) {
        RingBuffer<String> buffer = new RingBuffer<String>(16, 0, 64L);
        for (int i = 0; i < 100; i++) {
            String txt = i + "";
            buffer.put(txt);
        }
        printWithNext(buffer, 0, 100);   // 打印全部 16个 [84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
        printWithNext(buffer, 84, 100);  // 打印全部 16个  [84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
        printWithNext(buffer, 98, 100);  // 打印98以后的 2个 [98, 99]
        printWithNext(buffer, 88, 100);  // 打印88以后的 [88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
        printWithNext(buffer, 86, 100);  // 打印86以后的 [86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99]
        printWithNext(buffer, 100, 100); // 打印 空

        System.out.println("****************************************");
        buffer = new RingBuffer<String>(8, 0, 64L);
        for (int i = 0; i < 67; i++) {
            String txt = i + "";
            buffer.put(txt);
        }

        printWithNext(buffer, 0, 67);  // 打印全部
        printWithNext(buffer, 62, 67); // 打印62以后的
        printWithNext(buffer, 66, 67); // 打印67以后的
        printWithNext(buffer, 64, 67); // 打印64以后的
        printWithNext(buffer, 59, 67); // 打印59以后的

        System.out.println("****************************************");
        printTryNext(buffer, 0, 67);  // 打印全部
        printTryNext(buffer, 62, 67); // 打印63以后的
        printTryNext(buffer, 66, 67); // 打印67以后的,即空
        printTryNext(buffer, 64, 67); // 打印65以后的
        printTryNext(buffer, 59, 67); // 打印60以后的


    }

    public static void printWithNext(RingBuffer<String> buffer, long seq, long length) {
        List<String> reps = buffer.withNext(seq);
        System.out.print(seq + "/" + (length - 1) + " withNext's length : " + reps.size() + ", withNext's values: ");
        System.out.println(Arrays.toString(reps.toArray()));
    }
    public static void printTryNext(RingBuffer<String> buffer, long seq, long length) {
        List<String> reps = buffer.tryNext(seq);
        System.out.print(seq + "/" + (length - 1) + " withNext's length : " + reps.size() + ", withNext's values: ");
        System.out.println(Arrays.toString(reps.toArray()));
    }

    private class Buffered<E> {
        Long key;
        E value;

        Buffered(Long key, E value) {
            this.value = value;
            this.key = key;
        }
    }
}