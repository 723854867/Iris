package com.busap.vcs.hlsmedia.http.hls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.busap.vcs.hlsmedia.http.HttpUploadDataFile;
import com.busap.vcs.hlsmedia.http.HttpUploadResponse;
import com.busap.vcs.hlsmedia.http.NettyHttpUtils;
import com.busap.vcs.hlsmedia.http.SimpleCallbackHttpClient;
import com.busap.vcs.hlsmedia.http.transcode.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.util.CharsetUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;

@Component("hlsUploadServerHandler")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HlsUploadServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private static final Logger log = LoggerFactory.getLogger(HlsUploadServerHandler.class);

    @Resource(name = "hlsSettings")
    HlsSettings settings;
    @Resource(name = "transcodeScheduler")
    TranscodeScheduler transcodeScheduler;
    @Resource(name = "simpleCallbackHttpClient")
    private SimpleCallbackHttpClient callbackClient;


    private boolean readingChunks;
    private List<FileUpload> videoFiles = null;
    private List<Video> videos = null;


    // Disk if size exceed
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private HttpPostRequestDecoder decoder;

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true; // should delete file
        // on exit (in normal exit)
        DiskFileUpload.baseDirectory = null; // system temp directory
        DiskAttribute.deleteOnExitTemporaryFile = true; // should delete file on
        // exit (in normal exit)
        DiskAttribute.baseDirectory = null; // system temp directory
    }

    public HlsUploadServerHandler() {
        super();
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if (decoder != null) {
            decoder.cleanFiles();
        }
    }

    /**
     * Returns {@code true} if the given message should be handled. If
     * {@code false} it will be passed to the next
     * {@link io.netty.channel.ChannelInboundHandler} in the
     * {@link io.netty.channel.ChannelPipeline}.
     */
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        if (super.acceptInboundMessage(msg)) {
            if (msg instanceof HttpRequest) {
                HttpRequest request = (HttpRequest) msg;
                if (request.getUri().startsWith(settings.uploadMappingURIPath)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) { //处理普通的请求
            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.getUri());
            String path = uri.getPath();
            if (path.equals(settings.uploadMappingURIPath)) { // GET请求,则返回上传页面
                index(ctx, request);
                return;
            }
            HttpMethod method = request.getMethod();
            if (method.equals(HttpMethod.OPTIONS)) { // 处理跨站上传, 处理Options方法
                Map<String, String> addHeaders = new HashMap<String, String>();
                addHeaders.put(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                NettyHttpUtils.sendResponse(ctx, request, null, addHeaders);
                return;
            } else if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)) {
                // 文件上传的正常方式, POST 或者 PUT 请求
                log.info(String.format("%s -- %s --> %s", ctx.channel(), request.getMethod(), uri));
                try {
                    decoder = new HttpPostRequestDecoder(factory, request);
                    readingChunks = HttpHeaders.isTransferEncodingChunked(request);
                    videoFiles = new ArrayList<FileUpload>();
                    videos = new ArrayList<Video>();
                } catch (ErrorDataDecoderException e) {
                    log.error("fail to create HttpPostRequestDecoder for ErrorDataDecoderException", e);
                    NettyHttpUtils.sendError(ctx, request, BAD_REQUEST, e.getMessage());
                    ctx.channel().close();
                    reset(true);
                    return;
                } catch (IncompatibleDataDecoderException e) {
                    // GET Method: should not try to create a HttpPostRequestDecoder
                    // So OK but stop here
                    log.error("fail to create HttpPostRequestDecoder for IncompatibleDataDecoderException", e);
                    NettyHttpUtils.sendError(ctx, request, BAD_REQUEST, e.getMessage());
                    reset(true);
                    return;
                } catch (Exception e) {
                    // Other Exception: Stop and Send Errors.
                    log.error("fail to create HttpPostRequestDecoder for Exception", e);
                    NettyHttpUtils.sendError(ctx, request, BAD_REQUEST, e.getMessage());
                    reset(true);
                    return;
                }

            } else {
                NettyHttpUtils.sendError(ctx, request, METHOD_NOT_ALLOWED);
                return;
            }

        }
        if (decoder != null && msg instanceof HttpContent) { // 处理chunk文件上传
            // New chunk is received
            HttpContent chunk = (HttpContent) msg;
            try {
                decoder.offer(chunk);
            } catch (ErrorDataDecoderException e) {
                log.error("fail to decode HttpContent", e);
                NettyHttpUtils.sendError(ctx, null, BAD_REQUEST, e.getMessage());
                reset(true);
                return;
            }
            // example of reading chunk by chunk (minimize memory usage due to
            // Factory)
            try {
                readHttpDataChunkByChunk(ctx);
            } catch (IOException e) {
                log.error("fail to decode HttpContentData chunk", e);
                NettyHttpUtils.sendError(ctx, null, BAD_REQUEST, e.getMessage());
                reset(true);
                return;
            }
            // example of reading only if at the end
            if (chunk instanceof LastHttpContent) {
                // 发送响应给客户端
                writeResponse(ctx);
                // 开启转码任务
                transcodeVideos();
                reset(false);
            }
        }

    }

    private void reset(boolean isError) {
        readingChunks = false;
        // 业务临时数据的清理
        if (isError) { //如果是出错, 需要删除已经上传的mp4文件
            if (videoFiles != null) {
                for (FileUpload vFile : videoFiles) {
                    decoder.removeHttpDataFromClean(vFile);
                }
            }
        }
        videoFiles = null;
        videos = null;
        // destroy the decoder to release all resources
        decoder.destroy();
        decoder = null;
    }

    /**
     * Example of reading request by chunk and getting values from chunk to
     * chunk
     */

    private void readHttpDataChunkByChunk(ChannelHandlerContext ctx) throws IOException {
        try {
            while (decoder.hasNext()) { //处理所有的上传文件, 当一个文件已经完整传输后, hasNext() 会返回 true
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        // new value
                        writeHttpData(data);
                    } finally {
                        data.release();
                    }
                }
            }
        } catch (EndOfDataDecoderException e) {
            // end
        }
    }

    // 处理
    private void writeHttpData(InterfaceHttpData data) throws IOException {
        if (data.getHttpDataType() == HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;
            if (fileUpload.isCompleted()) {
                videoFiles.add(fileUpload);
                // 有效数据检查
                if (fileUpload.getFile() == null) {
                    throw new IOException(String.format("\tbad request: miss file %s.\r\n", fileUpload.getFilename()));
                } else {
                    String filename = fileUpload.getFilename();
                    String ext = FilenameUtils.getExtension(filename);
                    if (!ext.equalsIgnoreCase("mp4")) {
                        throw new IOException(String.format("\tbad request: incorrect media file format %s.\r\n", fileUpload.getFilename()));
                    }
                }
                // 每个文件存放一个目录
                // 利用UUID生成文件夹名和新文件名
                String uuid = UUIDFilenameUtils.newUUID();
                String playId = UUIDFilenameUtils.toPlayID(uuid);
                String ext = FilenameUtils.getExtension(fileUpload.getFilename());
                // 新文件名
                String nFilename = UUIDFilenameUtils.toBasename(uuid);
                // 存放目标文件夹
                String dest = settings.mappingLocalPath + File.separator + UUIDFilenameUtils.toParentPath(uuid) + File.separator;
                // move into another File dest
                // 添加转码任务
                final Video v = new Video();
                FileUtils.forceMkdir(new File(dest));
                String path = dest + nFilename + "." + ext;
                fileUpload.renameTo(new File(path));
                // 计算下载地址

                v.setFilepath(path);
                v.setUuid(uuid);
                v.setTitle(fileUpload.getFilename());
                v.setPlayId(playId);
                // HTTP 属性
                v.setCharset(fileUpload.getCharset());
                v.setContentLength(fileUpload.length());
                v.setContentTransferEncoding(fileUpload.getContentTransferEncoding());
                v.setContentType(fileUpload.getContentType());
                videos.add(v);
            } else {
                throw new IOException(String.format("\"\\tFile %s to be continued but should not!\\r\\n\"", fileUpload.getFilename()));
            }
        }
    }

    private void transcodeVideos() {
        for (Video v : videos) {
            transcode(v);
        }
    }

    private void transcode(final Video v) {
        DefaultTranscode transcode = new DefaultTranscode(v, new TranscodeResultHandler() {
            @Override
            public void onProcessComplete(Video video) {
                // 通知其他服务器,处理完成
                feedback(v);
            }

            @Override
            public void onProcessFailed(Video video, Throwable e) {
                // ignored
                File f = new File(video.getFilepath());
                FileUtils.deleteQuietly(f);
                // 通知服务器,转码错误
                feedback(v);
            }
        });
        try {
            transcodeScheduler.execute(transcode);
        } catch (Exception e) {
            // 通知服务器,转码错误
            File f = new File(v.getFilepath());
            FileUtils.deleteQuietly(f);
            // 通知服务器,转码错误
            feedback(v);
        }

    }

    private void feedback(Video v) {
        if (callbackClient != null) {
            try {
                callbackClient.callback(v);
            } catch (Exception ce) {
                //ignored.
                // TODO 视频转码状态查询接口
                log.error("fail to send feedback to" + callbackClient, ce);
            }
        }
    }

    private void writeResponse(ChannelHandlerContext ctx) {
        HttpUploadResponse response = new HttpUploadResponse();
        response.isChunked = readingChunks;
        response.isMultipart = decoder.isMultipart();
        response.isFinished = true;
        for (Video v : videos) {
            HttpUploadDataFile httpDataFileuploadDataFile = new HttpUploadDataFile();
            httpDataFileuploadDataFile.filename = v.getTitle();
            httpDataFileuploadDataFile.contentTransferEncoding = v.getContentTransferEncoding();
            httpDataFileuploadDataFile.contentType = v.getContentType();
            httpDataFileuploadDataFile.charset = v.getCharset();
            httpDataFileuploadDataFile.contentLength = v.getContentLength();
            httpDataFileuploadDataFile.playId = v.getPlayId();
            response.httpDataFileuploadDataFiles.add(httpDataFileuploadDataFile);
        }
        String responseContent = JSON.toJSONString(response, SerializerFeature.WriteClassName, SerializerFeature.NotWriteRootClassName);
        Map<String, String> addHeaders = new HashMap<String, String>();
        addHeaders.put(CONTENT_TYPE, "application/json; charset=UTF-8");
        addHeaders.put(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        NettyHttpUtils.sendResponse(ctx, null, responseContent, addHeaders);
    }


    // print a test upload html
    private void index(ChannelHandlerContext ctx, HttpRequest request) {
        // print several HTML forms
        // Convert the response content to a ChannelBuffer.
        StringBuilder responseContent = new StringBuilder();
        responseContent.setLength(0);

        // create Pseudo Menu
        responseContent.append("<html>");
        responseContent.append("<head>");
        responseContent.append("<title>Hls Upload Test Form</title>\r\n");
        responseContent.append("<script type=\"text/javascript\">\r\n");
        responseContent.append("function displayHlsFiles(input){\r\n");
        responseContent.append("var filelist = document.getElementById('filelist');\r\n");
        responseContent.append("for (var i = 0; i < filelist.childNodes.length; i++){ filelist.removeChild(filelist.childNodes[i]); }\r\n");
        responseContent.append("for (var i = 0; i < input.files.length; i++) {\r\n");
        responseContent.append("   var mbs = (input.files[i].size / (1024*1024)).toFixed(2);\r\n");
        responseContent.append("   var filename = input.files[i].name;\r\n");

        responseContent.append("   var texts = filename + \" [ \"+mbs+\" mb] \";\r\n");
        responseContent.append("   var li = document.createElement('li');\r\n");
        responseContent.append("   li.innerHTML = texts;\r\n");
        responseContent.append("   filelist.appendChild(li);\r\n");
        responseContent.append("}\r\n");
        responseContent.append("}\r\n");
        responseContent.append("</script>\r\n");
        responseContent.append("</head>\r\n");
        responseContent.append("<body>");
        responseContent.append("<table border=\"0\">");
        responseContent.append("<tr>");
        responseContent.append("<td>");
        responseContent.append("<h1>Hls Upload Test Form</h1>");
        responseContent.append("</td>");
        responseContent.append("</tr>");
        responseContent.append("</table>\r\n");

        // POST with enctype="multipart/form-data"
        responseContent.append("<FORM ACTION=\"" + settings.uploadMappingURIPath + "/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
        responseContent.append("<p>POST MULTIPART FORM <INPUT TYPE=\"submit\" NAME=\"submit\"></INPUT><HR WIDTH=NOSHADE color=\"blue\"></p>");
        responseContent.append("<input size=36 type=file id=\"hlsfiles\" name=\"hlsfile\" multiple=true onchange=\"displayHlsFiles(this);\">");
        responseContent.append("<ul id=\"filelist\"><ul/>\r\n");
        responseContent.append("</FROM>\r\n");

        responseContent.append("</body>");
        responseContent.append("</html>");

        ByteBuf buf = copiedBuffer(responseContent.toString(), CharsetUtil.UTF_8);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        response.headers().set(CONTENT_LENGTH, buf.readableBytes());

        // Write the response.
        ChannelFuture future = ctx.channel().writeAndFlush(response);
        if (NettyHttpUtils.toClose(request)) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(ctx.channel().toString(), cause);
        ctx.channel().close();
    }

}
