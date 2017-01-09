package com.busap.vcs.operate.utils;

import java.io.Serializable;
import java.util.Arrays;

public class Pair<U, V>
        implements Serializable, Cloneable {
    private static final long serialVersionUID = 5429741192255267602L;
    private U first;
    private V second;

    public Pair() {
    }

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return this.first;
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public V getSecond() {
        return this.second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('<');
        buf.append(this.first);
        buf.append(',');
        buf.append(this.second);
        buf.append('>');
        return buf.toString();
    }

    public int hashCode() {
        int hash1 = 0;
        if (this.first != null) {
            hash1 = this.first.hashCode();
        }
        int hash2 = 0;
        if (this.second != null) {
            hash2 = this.second.hashCode();
        }
        return hash1 * 31 + hash2;
    }

    public boolean equals(Object o) {
        if ((o instanceof Pair)) {
            Pair p = (Pair) o;
            return Arrays.equals(new Object[]{this.first, this.second}, new Object[]{p.first, p.second});
        }
        return false;
    }

    public Pair<U, V> clone()
            throws CloneNotSupportedException {
        return (Pair) super.clone();
    }
}
