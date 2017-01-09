package com.busap.vcs.hlsmedia.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static io.netty.buffer.Unpooled.EMPTY_BUFFER;
import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_MODIFIED;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by djyin on 7/29/2014.
 */
public class NettyHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(NettyHttpUtils.class);
    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;

    public static void sendResponse(ChannelHandlerContext ctx, HttpRequest request, String responseContent, String contentType) {
        // Convert the response content to a ChannelBuffer.
        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_TYPE, contentType);
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        // Decide whether to close the connection or not.
        // Write the response.
        ChannelFuture future = ctx.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static void sendResponse(ChannelHandlerContext ctx, HttpRequest request, String responseContent, Map<String, String> headers) {
        // Convert the response content to a ChannelBuffer.
        ByteBuf buf;
        if (responseContent == null) {
            buf = EMPTY_BUFFER;
        } else {
            buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        }
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());
        for (Map.Entry<String, String> e : headers.entrySet()) {
            response.headers().add(e.getKey(), e.getValue());
        }
        // Decide whether to close the connection or not.
        // Write the response.
        ChannelFuture future = ctx.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }


    public static void sendResponse(ChannelHandlerContext ctx, HttpRequest request, String responseContent) {
        sendResponse(ctx, request, responseContent, "text/plain; charset=UTF-8");
    }

    public static void sendRedirect(ChannelHandlerContext ctx, HttpRequest request, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static void sendError(ChannelHandlerContext ctx, HttpRequest request, HttpResponseStatus status) {
        sendError(ctx, request, status, "Failure: " + status + "\r\n");
    }

    public static void sendError(ChannelHandlerContext ctx, HttpRequest request, HttpResponseStatus status, String content) {
        sendError(ctx, request, status, content, "text/plain; charset=UTF-8");
    }

    public static void sendError(ChannelHandlerContext ctx, HttpRequest request, HttpResponseStatus status, String content, String contentType) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, contentType);
        // Close the connection as soon as the error message is sent.
        ChannelFuture future = ctx.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        log.info(String.format("%s -- %s --> %s = %s", ctx.channel(), request.getMethod(), request.getUri(), status));
    }

    /**
     * When file timestamp is the same as what the browser is sending up, send a "304 Not Modified"
     *
     * @param ctx Context
     */
    public static void sendNotModified(ChannelHandlerContext ctx, HttpRequest request) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
        setDateHeader(response);

        // Close the connection as soon as the error message is sent.
        ChannelFuture future = ctx.writeAndFlush(response);
        // Close the connection after the write operation is done if necessary.
        if (toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * Sets the Date header for the HTTP response
     *
     * @param response HTTP response
     */
    public static void setDateHeader(FullHttpResponse response) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        Calendar time = new GregorianCalendar();
        response.headers().set(DATE, dateFormatter.format(time.getTime()));
    }

    /**
     * Sets the Date and Cache headers for the HTTP Response
     *
     * @param response    HTTP response
     * @param fileToCache file to extract content type
     */
    public static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        // Date header
        Calendar time = new GregorianCalendar();
        response.headers().set(DATE, dateFormatter.format(time.getTime()));

        // Add cache headers
        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
        response.headers().set(EXPIRES, dateFormatter.format(time.getTime()));
        response.headers().set(CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.headers().set(
                LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
    }

    /**
     * Sets the content type header for the HTTP Response
     *
     * @param response HTTP response
     * @param file     file to extract content type
     */
    public static void setContentTypeHeader(HttpResponse response, File file) {
        response.headers().set(CONTENT_TYPE, MimeTypes.getContentType(file.getPath()));
    }


    /**
     * Decide whether to close the connection or not.
     *
     * @param request
     * @return
     */
    public static boolean toClose(HttpRequest request) {
        //return true;
        if (request != null)
            return !HttpHeaders.isKeepAlive(request);
        else return true;
    }


}
