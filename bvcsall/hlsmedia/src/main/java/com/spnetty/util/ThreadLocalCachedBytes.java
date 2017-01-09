package com.spnetty.util;


import java.lang.ref.SoftReference;

/**
 *
 * 截取自fastjson
 *
 * 提供线程绑定数组缓存功能
 *
 * 可以避免频繁创建byte数组对象
 *
 * 由于socket读写缓冲区一般不会超过64k,所以最大长度限制为256K，超过256K的数据不进行缓存。
 *
 * socket读写缓冲区,一般是小于64k,常见设置大小是(1500bytes+40bytes)=4.5k
 *
 * netty服务器的配置如果没有修改,读写缓冲区则为146432bytes,即146k.
 *
 * 注意!!!
 *
 * 在同一个JVM中, 这个类只能用在一个地方, 在两种地方用, 请继承这个类.
 *
 */
public class ThreadLocalCachedBytes {

    private final static int BYTE_CACHE_INIT_SIZE = 1024;
    // 当申请的byte超过 256k 时, 会直接返回一个新创建的byte[]
    private final static int BYTE_CACHE_MAX_SIZE = 1024 * 256;

    // 使用 SoftReference 保证内存弹性
    private final ThreadLocal<SoftReference<byte[]>> byteBufLocal;


    public ThreadLocalCachedBytes(ThreadLocal<SoftReference<byte[]>> byteBufLocal){
        this.byteBufLocal = byteBufLocal;
    }

    public void clear() {
        byteBufLocal.set(null);
    }

    public byte[] getBytes(int length) {
        SoftReference<byte[]> ref = byteBufLocal.get();
        if (ref == null) {
            return allocateBytes(length);
        }
        byte[] bytes = ref.get();
        if (bytes == null) {
            return allocateBytes(length);
        }
        if (bytes.length < length) {
            bytes = allocateBytes(length);
        }

        return bytes;
    }

    private int getAllocateLength(int init, int max, int length) {
        int value = init;
        while (true) {
            if (value >= length) {
                return value;
            }
            value *= 2;
            if (value > max) {
                break;
            }
        }
        return length;
    }

    private byte[] allocateBytes(int length) {
        int allocateLength = getAllocateLength(BYTE_CACHE_INIT_SIZE,
                BYTE_CACHE_MAX_SIZE, length);
        if (allocateLength <= BYTE_CACHE_MAX_SIZE) {
            byte[] bytes = new byte[allocateLength];
            byteBufLocal.set(new SoftReference<byte[]>(bytes));
            return bytes;
        }
        return new byte[length];
    }



}