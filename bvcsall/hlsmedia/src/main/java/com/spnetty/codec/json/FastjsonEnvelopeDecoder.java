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
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

/**
 * Created by
 * User: djyin
 * Date: 1/15/14
 * Time: 3:40 PM
 * 配合frameEncoder使用
 * p.addLast("frameEncoder", new LengthFieldPrepender(8));
 * p.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 8, 0, 8));
 */
public class FastjsonEnvelopeDecoder extends ByteToMessageCodec<Object> {

    private static final ThreadLocal<SoftReference<byte[]>> byteBufLocal = new ThreadLocal<SoftReference<byte[]>>();
    ThreadLocalCachedBytes cache = new ThreadLocalCachedBytes(byteBufLocal);
    // 默认的配置

    //new Feature[]{
    //    Feature.IgnoreNotMatch,Feature.AllowISO8601DateFormat
    //};

    /**
     * 默认测参数
     */
    public FastjsonEnvelopeDecoder() {
        super();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg instanceof Envelope) {
            byte[] inenv = JSON.toJSONBytes(msg, SerializerFeature.WriteClassName, SerializerFeature.NotWriteRootClassName);
            out.writeBytes(inenv);
        } else {
            // to envelope
            Envelope ev = new Envelope(msg);
            byte[] inenv = JSON.toJSONBytes(ev, SerializerFeature.WriteClassName, SerializerFeature.NotWriteRootClassName);
            out.writeBytes(inenv);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 优化,不要每次创建byte[],减少gc压力
        int len = in.readableBytes();
        byte[] buff = cache.getBytes(len);
        in.readBytes(buff, 0, len);
        Envelope obj = JSON.parseObject(buff, 0, len, ThreadLocalCache.getUTF8Decoder(), Envelope.class);
        out.add(obj);
    }

}
