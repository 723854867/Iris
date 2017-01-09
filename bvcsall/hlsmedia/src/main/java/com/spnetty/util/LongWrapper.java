package com.spnetty.util;

/**
 * 将long包裹成对象
 */
public class LongWrapper {
    private long value = 0;

    public LongWrapper(long value) {

        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
