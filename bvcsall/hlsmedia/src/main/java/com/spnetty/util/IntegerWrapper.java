package com.spnetty.util;

/**
 * 将int包裹成对象
 */
public class IntegerWrapper {

    int value = 0;

    public IntegerWrapper(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
