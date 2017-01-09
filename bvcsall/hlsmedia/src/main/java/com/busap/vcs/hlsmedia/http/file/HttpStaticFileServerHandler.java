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
package com.busap.vcs.hlsmedia.http.file;

import com.busap.vcs.hlsmedia.http.NettyHttpUtils;
import com.busap.vcs.hlsmedia.http.Tuple;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.TypeParameterMatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * A simple handler that serves incoming HTTP requests to send their respective
 * HTTP responses.  It also implements {@code 'If-Modified-Since'} header to
 * take advantage of browser cache, as described in
 * <a href="http://tools.ietf.org/html/rfc2616#section-14.25">RFC 2616</a>.
 * <p/>
 * <h3>How Browser Caching Works</h3>
 * <p/>
 * Web browser caching works with HTTP headers as illustrated by the following
 * sample:
 * <ol>
 * <li>Request #1 returns the content of {@code /file1.txt}.</li>
 * <li>Contents of {@code /file1.txt} is cached by the browser.</li>
 * <li>Request #2 for {@code /file1.txt} does return the contents of the
 * file again. Rather, a 304 Not Modified is returned. This tells the
 * browser to use the contents stored in its cache.</li>
 * <li>The server knows the file has not been modified because the
 * {@code If-Modified-Since} date is the same as the file's last
 * modified date.</li>
 * </ol>
 * <p/>
 * <pre>
 * Request #1 Headers
 * ===================
 * GET /file1.txt HTTP/1.1
 *
 * Response #1 Headers
 * ===================
 * HTTP/1.1 200 OK
 * Date:               Tue, 01 Mar 2011 22:44:26 GMT
 * Last-Modified:      Wed, 30 Jun 2010 21:36:48 GMT
 * Expires:            Tue, 01 Mar 2012 22:44:26 GMT
 * Cache-Control:      private, max-age=31536000
 *
 * Request #2 Headers
 * ===================
 * GET /file1.txt HTTP/1.1
 * If-Modified-Since:  Wed, 30 Jun 2010 21:36:48 GMT
 *
 * Response #2 Headers
 * ===================
 * HTTP/1.1 304 Not Modified
 * Date:               Tue, 01 Mar 2011 22:44:28 GMT
 *
 * </pre>
 */
public class HttpStaticFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger log = LoggerFactory.getLogger(SimpleChannelInboundHandler.class);
    /**
     * 默认的URI地址
     */
    public static final String DEFAULT_MAPPING_LOCAL_PATH = "./files/";
    /**
     * 默认的URI地址
     */
    public static final String DEFAULT_MAPPING_URI_PATH = "/files";


    /**
     * 本地目录地址
     */
    protected final String mappingLocalPath;

    /**
     * 接收请求的URI地址
     */
    protected final String mappingUriPath;
    /**
     * 是否允许列出本地文件列表
     */
    protected final boolean listable;

    private final TypeParameterMatcher matcher;

    public HttpStaticFileServerHandler(String mappingUriPath, String mappingLocalPath) {
        this(mappingUriPath, mappingLocalPath, true);
    }

    public HttpStaticFileServerHandler(String mappingUriPath, String mappingLocalPath, boolean listable) {
        super();
        if (mappingLocalPath == null) {
            this.mappingLocalPath = DEFAULT_MAPPING_LOCAL_PATH;
        } else {
            this.mappingLocalPath = mappingLocalPath;
        }
        if (mappingUriPath == null) {
            this.mappingUriPath = DEFAULT_MAPPING_URI_PATH;
        } else {
            if (!mappingUriPath.startsWith("/")) {
                mappingUriPath = "/" + mappingUriPath;
            }
            this.mappingUriPath = mappingUriPath;
        }
        this.listable = listable;
        matcher = TypeParameterMatcher.find(this, SimpleChannelInboundHandler.class, "I");
    }

    /**
     * Returns {@code true} if the given message should be handled. If {@code false} it will be passed to the next
     * {@link io.netty.channel.ChannelInboundHandler} in the {@link io.netty.channel.ChannelPipeline}.
     */
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (matcher.match(msg)) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            if (fullHttpRequest.getUri().startsWith(this.mappingUriPath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据URI路径找到对应的文件
     *
     * @param path 路径,比如 /files/xxxx.jpg
     * @return
     */
    public File find(String path) {
        // Decode the path.
        try {
            String requestUri = URLDecoder.decode(path, "UTF-8");
            // 除去?后的部分
            int idx = requestUri.indexOf('?');
            if (idx >= 0) {
                requestUri = requestUri.substring(0, idx);
            }
            // Convert to absolute path.
            path = mappingLocalPath + File.separator + requestUri;
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
        File file = new File(path);
        return file;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.getDecoderResult().isSuccess()) {
            NettyHttpUtils.sendError(ctx, request, BAD_REQUEST);
            return;
        }

        if (!request.getMethod().equals(GET)) {
            NettyHttpUtils.sendError(ctx, request, METHOD_NOT_ALLOWED);
            return;
        }

        final String uri = request.getUri();
        final String path = uri.substring(getMappingUriPath().length());
        final File file = find(path);

        if (file == null) {
            NettyHttpUtils.sendError(ctx, request, FORBIDDEN);
            return;
        }
        if (file.isHidden() || !file.exists()) {
            NettyHttpUtils.sendError(ctx, request, NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(ctx, request, file);
            } else {
                NettyHttpUtils.sendRedirect(ctx, request, uri + '/');
            }
            return;
        }

        // Cache Validation
        String ifModifiedSince = request.headers().get(IF_MODIFIED_SINCE);
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(NettyHttpUtils.HTTP_DATE_FORMAT, Locale.US);
            Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);

            // Only compare up to the second because the datetime format we send to the client
            // does not have milliseconds
            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
            long fileLastModifiedSeconds = file.lastModified() / 1000;
            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
                NettyHttpUtils.sendNotModified(ctx, request);
                return;
            }
        }

        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException ignore) {
            NettyHttpUtils.sendError(ctx, request, NOT_FOUND);
            return;
        }
        final long fileLength = raf.length();
        final boolean isKeeplive = HttpHeaders.isKeepAlive(request);
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

        NettyHttpUtils.setContentTypeHeader(response, file);
        NettyHttpUtils.setDateAndCacheHeaders(response, file);
        if (isKeeplive) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        String rangeHeader = request.headers().get(CONTENT_RANGE);
        final Long start;
        final Long end;
        final Long length;
        Tuple<Long, Long>
                range = parseRange(rangeHeader, fileLength);

        if (range == null) {
            HttpHeaders.setContentLength(response, fileLength);
            start = 0L;
            end = fileLength - 1;
            length = fileLength;
        } else {
            length = range.getV() - range.getK() + 1;
            HttpHeaders.setContentLength(response, length);
            response.headers().set(CONTENT_RANGE, "bytes " + range.getK() + "-" + range.getV() + "/" + fileLength);
            start = range.getK();
            end = range.getV();

        }
        // Write the initial line and the header.
        ChannelFuture sendFileFuture = ctx.write(response);

        //根据文件大小尺寸判断是否使用chunk模式
        // 大于500k的文件使用chunk模式
        if (fileLength > 1024 * 500) {
            // Write the content in chunks. HTTP Chunk 方式
            // 这个模式兼容性不好.... 经常会出现 net:ERROR CONTENT LENGTH NOT MATCH
            sendFileFuture =
                    ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, start, length, 8192)));
            sendFileFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info(String.format("%s File Transfer (to %s) is completed [chunked]. File is %s. Content range is %s - %s. Content Length is %s.", future.channel(), future.channel().remoteAddress(), file, start, end, length));
                }
            });

        } else {
            // Write the content in stream 标准文件流的方式
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(raf.getChannel(), start, length));
            sendFileFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    log.info(String.format("%s File Transfer (to %s) is completed [stream]. File is %s. Content range is %s - %s. Content Length is %s.", future.channel(), future.channel().remoteAddress(), file, start, end, length));
                }
            });
            // Write the end marker
            sendFileFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }
        // Decide whether to close the connection or not.
        if (!NettyHttpUtils.toClose(request)) {
            // Close the connection when the whole content is written out.
            sendFileFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private static final Pattern RANGE_HEADER = Pattern.compile("bytes=(\\d+)\\-(\\d+)?");

    private Tuple<Long, Long> parseRange(String header, long availableLength) {
        if (StringUtils.isEmpty(header)) {
            return null;
        }
        Matcher m = RANGE_HEADER.matcher(header);
        if (!m.matches()) {
            //ignored
            return null;
        }
        Tuple<Long, Long> result = new Tuple<Long, Long>();
        result.setK(Long.parseLong(m.group(1)));
        if (StringUtils.isNotEmpty(m.group(2))) {
            result.setV(Long.parseLong(m.group(2)));
        } else {
            result.setV(availableLength - 1);
        }
        if (result.getV() < result.getK()) {
            return null;
        }
        if (result.getV() >= availableLength) {
            result.setV(availableLength - 1);
        }

        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(INTERNAL_SERVER_ERROR.toString(), cause);
        if (ctx.channel().isActive()) {
            NettyHttpUtils.sendError(ctx, null, INTERNAL_SERVER_ERROR);
        }
    }

    // 隐藏文件 以.开头的
    private static final Pattern NOT_ALLOWED_FILE_NAME = Pattern.compile("[\\.][.]*");

    private void sendListing(ChannelHandlerContext ctx, FullHttpRequest request, File dir) {
        if (!isListable()) {
            NettyHttpUtils.sendError(ctx, request, FORBIDDEN);
            return;
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();

        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append("Listing of: ");
        buf.append(dirPath);
        buf.append("</title></head><body>\r\n");

        buf.append("<h3>Listing : ");
        buf.append(dirPath);
        buf.append("</h3>\r\n");

        buf.append("<ul>");
        buf.append("<li><a href=\"../\">..</a></li>\r\n");
        List<File> files = Arrays.asList(dir.listFiles());
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;
                return o1.getName().compareTo(o2.getName());
            }
        });
        for (File f : files) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }

            String name = f.getName();
            if (NOT_ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>");
            if (f.isDirectory()) {
            } else {
                buf.append("&nbsp;&nbsp;");
            }
            buf.append("<a href=\"");
            try {
                buf.append(URLEncoder.encode(name, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                //ignored
            }
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");

        }

        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();

        // Close the connection as soon as the error message is sent.
        ChannelFuture future = ctx.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (NettyHttpUtils.toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public String getMappingLocalPath() {
        return mappingLocalPath;
    }

    public String getMappingUriPath() {
        return mappingUriPath;
    }

    public boolean isListable() {
        return listable;
    }

}
