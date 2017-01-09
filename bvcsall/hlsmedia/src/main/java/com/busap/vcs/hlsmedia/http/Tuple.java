package com.busap.vcs.hlsmedia.http;

/**
 * Created by djyin on 8/7/2014.
 */
public class Tuple<K, V> {
    K k;
    V v;

    public V getV() {
        return v;
    }

    public void setV(V second) {
        this.v = second;
    }

    public K getK() {
        return k;
    }

    public void setK(K first) {
        this.k = first;
    }
}
