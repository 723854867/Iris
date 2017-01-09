package com.busap.vcs.hlsmedia.http;

import com.busap.vcs.hlsmedia.http.hls.Video;
import com.busap.vcs.hlsmedia.http.hls.Video.STATUS;
import com.spnetty.netty.AbstractNettyClient;
import com.spnetty.server.util.SimpleSpringCtxLaunchUtils;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一个简单的发送HTTP GET请求的client
 *
 * @author djyin
 */
@Service("simpleCallbackHttpClient")
public class SimpleCallbackHttpClient extends AbstractNettyClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleCallbackHttpClient.class);

    SimpleHttpClientResponseHandler handler;

    @Autowired
    public SimpleCallbackHttpClient(@Value("${hls.callback.update.status.uri}") final String uri,
                                    @Value("${hls.callback.connect.workers}") final int workers,
                                    @Value("${hls.callback.connect.timoutmillis}") final int connectTimeoutMillis) throws URISyntaxException {
        super(uri);
        Map<ChannelOption<?>, Object> channelOptions = new HashMap<ChannelOption<?>, Object>();
        channelOptions.put(ChannelOption.CONNECT_TIMEOUT_MILLIS,connectTimeoutMillis);
        setChannelOptions(channelOptions);
        setWorkers(workers);

    }
    @PostConstruct
    public void init() {
        super.init();
    }

    public static SimpleCallbackHttpClient getInstance() {
        // 获取执行队列
        SimpleCallbackHttpClient instance = null;
        if (SimpleSpringCtxLaunchUtils.ctx != null) {
            try {
                instance = SimpleSpringCtxLaunchUtils.ctx.getBean("simpleCallbackHttpClient", SimpleCallbackHttpClient.class);
            } catch (NoSuchBeanDefinitionException e) {
                // ignored
                logger.error("miss threadPoolTaskExecutor in spring context", e);
            }
        }
        return instance;
    }

    @Override
    public ChannelInitializer initChannel() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("codec", new HttpClientCodec());
                // Remove the following line if you don't want automatic content
                // decompression.
                pipeline.addLast("inflater", new HttpContentDecompressor());
                // to be used since huge file transfer
                pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
                pipeline.addLast("handler", new SimpleChannelInboundHandler<HttpObject>() {
                    private boolean readingChunks;
                    final StringBuilder contentBuffer = new StringBuilder();
                    int httpResponseStatus = 0;

                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
                        if (msg instanceof HttpResponse) {
                            HttpResponse response = (HttpResponse) msg;
                            httpResponseStatus = response.getStatus().code();
                            if (httpResponseStatus == HttpResponseStatus.OK.code() && HttpHeaders.isTransferEncodingChunked(response)) {
                                readingChunks = true;
                            }
                        }
                        if (msg instanceof HttpContent) {
                            HttpContent chunk = (HttpContent) msg;
                            String content = chunk.content().toString(CharsetUtil.UTF_8);
                            contentBuffer.append(content);
                            if (chunk instanceof LastHttpContent) {
                                String resp = contentBuffer.toString();
                                if (handler != null) {
                                    handler.onRecevied(ctx, httpResponseStatus, resp);
                                } else {
                                    // only print
                                    logger.info(String.format("--%s--> \r\n %s", httpResponseStatus, resp));
                                }
                                readingChunks = false;
                                contentBuffer.setLength(0);
                            }
                        }
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                        logger.error("HTTP Cilent fail to execute.", cause);
                        readingChunks = false;
                        contentBuffer.setLength(0);
                        httpResponseStatus = 0;
                    }
                });

            }
        };
    }

    public interface SimpleHttpClientResponseHandler {
        public void onRecevied(ChannelHandlerContext ctx, int status, String content);
    }

    public void callback(Video video) {
        // Prepare the HTTP request.
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("uuid", video.getUuid());
        variables.put("status", video.getStatus().toString());
        variables.put("w", Integer.toString(video.getH()));
        variables.put("h", Integer.toString(video.getW()));
        variables.put("duration", Double.toString(video.getDuration()));
        variables.put("error", video.getError());
        String full = substituteVariables(getUrl(), variables);
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, full);
        HttpHeaders headers = request.headers();
        headers.set(HttpHeaders.Names.HOST, uri.getHost());
        headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        headers.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP + ',' + HttpHeaders.Values.DEFLATE);
        headers.set(HttpHeaders.Names.USER_AGENT, "Busap Media Server");
        logger.info(String.format("callback %s", full));
        sendNextAndFlush(request);
    }

    public static List<String> variableNames(String template) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(template);
        List<String> buffer = new ArrayList<String>();
        while (matcher.find()) {
            buffer.add(matcher.group(1));
        }
        return buffer;
    }

    public static String substituteVariables(String template, Map<String, String> variables) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(template);
        // StringBuilder cannot be used here because Matcher expects
        // StringBuffer
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            if (variables.containsKey(matcher.group(1))) {
                String replacement = variables.get(matcher.group(1));
                // quote to work properly with $ and {,} signs
                matcher.appendReplacement(buffer, replacement != null ? Matcher.quoteReplacement(replacement) : "null");
            }
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, URISyntaxException {
        Video video = new Video();
        video.setUuid("fa061a6e/a99a/403e/a048/9542dd710b4e");
        video.setStatus(STATUS.transcode_ok);
        video.setH(720);
        video.setW(1280);
        video.setDuration((double) 10000);
        // http://10.18.22.12:8080/restadmin/video/videocallback?playKey=xxx&flowStat=xxx&w=xxx&h=xxx
        String url = "http://10.18.22.12:8080/restadmin/video/videocallback?playKey=${uuid}&flowStat=${status}&w=${w}&h=${h}&duration=${duration}&error=${error}";

        try {
            SimpleCallbackHttpClient client = new SimpleCallbackHttpClient(url, 2, 3000);
            client.init();
            client.callback(video);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
