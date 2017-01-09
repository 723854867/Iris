package com.busap.vcs.hlsmedia.http;

import com.busap.vcs.hlsmedia.http.file.HttpStaticFileServerHandler;
import com.busap.vcs.hlsmedia.http.file.SiteDefFileServerHandler;
import com.busap.vcs.hlsmedia.http.hls.HlsPlayFileServerHandler;
import com.busap.vcs.hlsmedia.http.hls.HlsSettings;
import com.busap.vcs.hlsmedia.http.hls.HlsUploadServerHandler;
import com.spnetty.server.TcpSocketServer;
import com.spnetty.server.util.SimpleSpringCtxLaunchUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;

/**
 * Created by djyin on 14-5-26.
 * HLS服务器
 */
@Component
public class HLSMediaServer extends TcpSocketServer {

    private static final Logger log = LoggerFactory.getLogger(HLSMediaServer.class);
    @Resource(name = "hlsSettings")
    HlsSettings settings = null;

    @Override
    public ChannelInitializer<SocketChannel> channelInitializer() {

        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ChannelPipeline p = ch.pipeline();

                //p.addLast("log", new LoggingHandler(LogLevel.INFO));

                p.addLast("HttpServerCodec", new HttpServerCodec());
                // Remove the following line if you don't want automatic content decompression.
                p.addLast("inflater", new HttpContentDecompressor());

                p.addLast("streamer", new ChunkedWriteHandler());
                //hsl的mp4文件上传文件处理
                HlsUploadServerHandler hlsUploadServerHandler = SimpleSpringCtxLaunchUtils.ctx.getBean("hlsUploadServerHandler", HlsUploadServerHandler.class);
                p.addLast("HlsUploadServerHandler", hlsUploadServerHandler);

                p.addLast("HttpRequest2FullHttpRequest", new SimpleChannelInboundHandler<HttpObject>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
                        if (msg.getDecoderResult().isSuccess() && msg instanceof HttpRequest) {
                            HttpRequest req = (HttpRequest) msg;
                            FullHttpRequest fullMsg = new DefaultFullHttpRequest(
                                    req.getProtocolVersion(), req.getMethod(), req.getUri(), Unpooled.EMPTY_BUFFER, false);
                            log.info(String.format("%s -- %s --> %s", ctx.channel(), fullMsg.getMethod(), fullMsg.getUri()));
                            fullMsg.setDecoderResult(req.getDecoderResult());
                            ctx.fireChannelRead(fullMsg);
                        }
                    }
                });
                //hls文件下载播放
                p.addLast("HlsPlayFileServerHandler", new HlsPlayFileServerHandler(settings));

                //静态文件处理
                p.addLast("HttpStaticFileServerHandler", new HttpStaticFileServerHandler(null, settings.mappingLocalPath));
                //flash播放器需要的 crossdomain.xml
                p.addLast("sitefile", new SiteDefFileServerHandler());
                // 404 错误处理
                p.addLast("404", new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        NettyHttpUtils.sendError(ctx, null, NOT_FOUND);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        log.error("exceptionCaught", cause);
                        if (ctx.channel().isActive()) {
                            NettyHttpUtils.sendError(ctx, null, INTERNAL_SERVER_ERROR);
                        } else {
                            ctx.channel().close();
                        }
                    }
                });
            }
        };
    }

    @Override
    public void run() throws Exception {
        super.run();
    }

}
