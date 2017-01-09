package com.busap.vcs.operate.thread;

/**
 * 线程对象
 */
public class KeyThreadLocal {
    private ThreadLocal<String> keyLocal;
    private static KeyThreadLocal local = new KeyThreadLocal();

    private KeyThreadLocal() {
        keyLocal = new ThreadLocal<String>();
    }

    public static KeyThreadLocal getInstanse() {
        return local;
    }

    public String getKeyLocal() {
        return keyLocal.get();
    }

    public void setKeyLocal(String key) {
        keyLocal.set(key);
    }
}
