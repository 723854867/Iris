package com.spnetty.codec;

/**
 * Created by
 * User: djyin
 * Date: 1/15/14
 * Time: 7:19 PM
 * 默认的协议信封，提供个json和kryo序列化时使用。protobuf使用自己的协议信封
 */
public class Envelope {

    Object body;

    /**
     * 类的CanonicalName名字
     */
    String cls;

    public Envelope() {
        // empty
    }

    public Envelope(Object body) {
        this.body = body;
        this.cls = body.getClass().getCanonicalName();
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
        this.cls = body.getClass().getCanonicalName();
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    @Override
    public String toString() {
        return "Envelope {" +
                "cls=" + cls +
                ", body=" + body +
                '}';
    }
}
