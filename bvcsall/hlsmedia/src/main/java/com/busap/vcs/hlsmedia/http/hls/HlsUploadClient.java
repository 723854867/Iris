/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.busap.vcs.hlsmedia.http.hls;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * 测试HLS文件上传 chunk模式
 */
public final class HlsUploadClient {

    static final String BASE_URL = System.getProperty("baseUrl", "http://127.0.0.1:63880/hls/upload");
    static final String FILE = System.getProperty("file", "upload.txt");

    public static void main(String[] args) throws Exception {
        String url = BASE_URL;
        String filepath = FILE;
        if (args != null && args.length > 0) {
            if (args.length > 1) {
                url = args[1];
                filepath = args[0];
            } else {
                filepath = args[0];
            }
        }
        if (url.endsWith("/")) {
            url = url + "formpostmultipart";
        } else {
            url = url + "/formpostmultipart";
        }
        URI uriSimple = new URI(url);

        String scheme = uriSimple.getScheme() == null ? "http" : uriSimple.getScheme();
        String host = uriSimple.getHost() == null ? "127.0.0.1" : uriSimple.getHost();
        int port = uriSimple.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }

        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            System.err.println("Only HTTP(S) is supported.");
            return;
        }

        final boolean ssl = "https".equalsIgnoreCase(scheme);
        final SslContext sslCtx;
        if (ssl) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        File file = new File(filepath);
        if (!file.canRead()) {
            throw new FileNotFoundException(FILE);
        }

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();

        // setup the factory: here using a mixed memory/disk based on size threshold
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE); // Disk if MINSIZE exceed

        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskFileUpload.baseDirectory = null; // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).handler(
                    new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            if (sslCtx != null) {
                                pipeline.addLast("ssl", sslCtx.newHandler(ch.alloc()));
                            }

                            pipeline.addLast("codec", new HttpClientCodec());

                            // Remove the following line if you don't want automatic content decompression.
                            pipeline.addLast("inflater", new HttpContentDecompressor());

                            // to be used since huge file transfer
                            pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

                            pipeline.addLast("handler", new HlsUploadClientHandler());
                        }
                    }
            );

            // Multipart Post form: factory used
            formpostmultipart(b, host, port, uriSimple, factory, file);
        } finally {
            // Shut down executor threads to exit.
            group.shutdownGracefully();

            // Really clean all temporary files if they still exist
            factory.cleanAllHttpDatas();
        }
    }


    /**
     * Multipart example
     */
    private static void formpostmultipart(
            Bootstrap bootstrap, String host, int port, URI uriSimple, HttpDataFactory factory,
            File file) throws Exception {
        // XXX /formpostmultipart
        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        // Wait until the connection attempt succeeds or fails.
        Channel channel = future.sync().channel();

        // Prepare the HTTP request.
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uriSimple.getPath());

        // Use the PostBody encoder
        HttpPostRequestEncoder bodyRequestEncoder =
                new HttpPostRequestEncoder(factory, request, true); // true => multipart


        // it is legal to add directly header or cookie into the request until finalize
        HttpHeaders headers = request.headers();
        headers.set(HttpHeaders.Names.HOST, host);
        headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        headers.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP + ',' + HttpHeaders.Values.DEFLATE);

        headers.set(HttpHeaders.Names.ACCEPT_CHARSET, "utf-8;q=0.7,*;q=0.7");
        headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "en");
        headers.set(HttpHeaders.Names.REFERER, uriSimple.toString());
        headers.set(HttpHeaders.Names.USER_AGENT, "Netty Simple Http Client side");
        headers.set(HttpHeaders.Names.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        //connection will not close but needed
        // headers.set("Connection","keep-alive");
        // headers.set("Keep-Alive","300");

        headers.set(
                HttpHeaders.Names.COOKIE, ClientCookieEncoder.encode(
                        new DefaultCookie("my-cookie", "foo"),
                        new DefaultCookie("another-cookie", "bar"))
        );

        // add Form attribute
        bodyRequestEncoder.addBodyFileUpload("hslfile", file, "application/x-zip-compressed", false);

        // finalize request
        bodyRequestEncoder.finalizeRequest();

        // send request
        ChannelFuture channelFuture = channel.write(request);

        // test if request was chunked and if so, finish the write
        if (bodyRequestEncoder.isChunked()) {
            channelFuture = channel.write(bodyRequestEncoder, channel.newProgressivePromise());
        }

        channelFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) { // total unknown
                    System.err.println(future.channel() + "Upload transfer progress: " + progress);
                } else {
                    System.err.println(future.channel() + "Upload transfer progress: " + progress + " / " + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) {
                System.err.println(future.channel() + "Upload complete.");
            }
        });
        channel.flush();
        channelFuture.sync();

        // Now no more use of file representation (and list of HttpData)
        bodyRequestEncoder.cleanFiles();

        // Wait for the server to close the connection.
        channel.closeFuture().sync();
    }


}
