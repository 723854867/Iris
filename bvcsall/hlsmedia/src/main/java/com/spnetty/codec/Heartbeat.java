package com.spnetty.codec;

/**
 * Created by
 * User: djyin
 * Date: 2/10/14
 * Time: 2:16 PM
 * 默认的心跳，protobuf使用自己的心跳对象
 */
public class Heartbeat {

    public enum Type {
        /**
         * <code>SYNC = 1;</code>
         */
        SYNC(),
        /**
         * <code>ACK = 2;</code>
         */
        ACK(),
        /**
         * <code>PSH = 3;</code>
         */
        PSH(),
        /**
         * <code>RST = 4;</code>
         */
        RST()
    }


    private Type type = Type.ACK;

    public Heartbeat(Type type) {
        super();
        this.type = type;
    }

    public Heartbeat() {
        super();
    }

    public Type getType() {
        return type;
    }

    public Heartbeat setType(Type type) {
        this.type = type;
        return this;
    }
}
