package com.spnetty.codec.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.ThreadLocalCache;
import com.spnetty.codec.Envelope;
import com.spnetty.util.ThreadLocalCachedBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by
 * User: djyin
 * Date: 1/15/14
 * Time: 3:40 PM
 * 和FastjsonEnvelopeDecoder相比,
 * 这个类优点是减少了传输量,缺点是解码的时候比较慢,而且Envelope的body里面有非基本数据类型时,会遗留fastjson的内置对象JsonObject
 */
public class FastjsonEnvelopePojoDecoder extends ByteToMessageCodec<Object> {

    private static final ThreadLocal<SoftReference<byte[]>> byteBufLocal = new ThreadLocal<SoftReference<byte[]>>();
    ThreadLocalCachedBytes cache = new ThreadLocalCachedBytes(byteBufLocal);
    // 默认的配置


    /**
     * 默认测参数
     */
    public FastjsonEnvelopePojoDecoder() {
        super();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof Envelope) {
            byte[] inenv = JSON.toJSONBytes(msg);
            out.writeBytes(inenv);
        } else {
            // to envelope
            Envelope ev = new Envelope(msg);
            byte[] inenv = JSON.toJSONBytes(ev);
            out.writeBytes(inenv);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 优化,不要每次创建byte[],减少gc压力
        int len = in.readableBytes();
        byte[] buff = cache.getBytes(len);
        in.readBytes(buff, 0, len);
        Envelope env = JSON.parseObject(buff, 0, len, ThreadLocalCache.getUTF8Decoder(), Envelope.class);
        Object body = JSON.parseObject(env.getBody().toString(), getClassByCName(env.getCls()));
        env.setBody(body);
        out.add(env);
    }

    private final Map<String, Class<?>> clzCache = new HashMap<String, Class<?>>();

    private Class<?> getClassByCName(String cname) throws ClassNotFoundException {
        Class<?> t = clzCache.get(cname);
        if (cname == null) {
            t = Class.forName(cname);
            clzCache.put(cname, t);
        }
        return t;
    }

}
