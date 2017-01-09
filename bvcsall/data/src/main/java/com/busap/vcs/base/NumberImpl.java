package com.busap.vcs.base;

/**
 * Created by
 * User: djyin
 * Date: 12/12/13
 * Time: 5:46 PM
 */
public class NumberImpl extends Number {

    private long l = 0;
    private double d = 0;
    /**
     * The Type.
     */
    private boolean type = true;

    public NumberImpl() {

    }

    public NumberImpl(Object obj) {
        this.parser(obj.toString());
    }

    public NumberImpl(String value) {
        this.parser(value);
    }

    private void parser(String value) {
        if (value.contains(".")) {
            this.type = false;
            this.d = Double.parseDouble(value);
        } else {
            this.type = false;
            this.l = Long.parseLong(value);
        }
    }

    @Override
    public int intValue() {
        if (type) {
            return (int) l;
        } else {
            return (int) d;

        }
    }

    @Override
    public long longValue() {
        if (type) {
            return l;
        } else {
            return (long) d;

        }
    }

    @Override
    public float floatValue() {
        if (type) {
            return l;
        } else {
            return (float) d;

        }
    }

    @Override
    public double doubleValue() {
        if (type) {
            return l;
        } else {
            return d;

        }
    }
}
