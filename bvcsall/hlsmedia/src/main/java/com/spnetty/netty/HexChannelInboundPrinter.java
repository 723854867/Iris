package com.spnetty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djyin on 6/10/2014.
 * 没啥用的代码..
 * 使用 io.netty.handler.logging.LoggingHandler 更好
 */
public class HexChannelInboundPrinter extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HexChannelInboundPrinter.class);
    final protected char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object in) throws Exception {
        if (in instanceof ByteBuf) {
            ByteBuf msg = ((ByteBuf) in).duplicate();
            final byte[] array;
            final int length = msg.readableBytes();
            if (msg.hasArray()) {
                array = msg.array();
            } else {
                array = new byte[length];
                msg.getBytes(msg.readerIndex(), array, 0, length);
            }
            log.debug("inbound hex: \r\n{}\r\n", bytesToHex(array));
        }
        ctx.fireChannelRead(in);
    }
}
