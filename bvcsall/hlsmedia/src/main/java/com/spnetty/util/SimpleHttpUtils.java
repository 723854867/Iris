package com.spnetty.util;

import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * Created by djyin on 7/11/2014.
 */
public class SimpleHttpUtils {


    public static final int CONNECT_TIMEOUT_MILLIS = 3000;
    public static final int READ_TIMEOUT_MILLIS = 3000;
    public static final int WRITE_TIMEOUT_MILLIS = 3000;

    public static final String HEADER_USER_AGENT_KEY = "user-agent";
    public static final String HEADER_USER_AGNET_VALUE = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static HttpResponse get(String url, String params, Map<String, String> headers) throws IOException {
        BufferedReader in = null;
        try {
            URL realUrl = params != null ? new URL(URLEncoder.encode(url + "?" + params.trim(), "UTF-8")) : new URL(URLEncoder.encode(url, "UTF-8"));
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.setReadTimeout(READ_TIMEOUT_MILLIS);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            // 处理http头信息
            if (headers == null || headers.isEmpty()) {
                // 设置通用的请求属性
                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty(HEADER_USER_AGENT_KEY, HEADER_USER_AGNET_VALUE);
            } else {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 建立连接,发送请求
            conn.connect();
            // 获取返回内容长度，单位字节
            int length = conn.getContentLength();
            // 获取代码返回值
            int respondCode = conn.getResponseCode();

            StringBuffer buff = length > 0 ? new StringBuffer(length) : new StringBuffer();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                buff.append("\n");
                buff.append(line);
            }

            HttpResponse r = new HttpResponse();
            r.setContent(buff.toString());
            r.setContentEncoding(conn.getContentEncoding());
            r.setContentType(conn.getContentType());
            r.setHttpCode(respondCode);

            return r;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignored
                ex.printStackTrace();
            }
        }
    }

    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static HttpResponse post(String url, Map<String, Object> params, Map<String, String> headers) throws IOException {
        BufferedReader in = null;
        DataOutputStream dos = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setReadTimeout(READ_TIMEOUT_MILLIS);
            conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 处理http头信息
            if (headers == null || headers.isEmpty()) {
                // 设置通用的请求属性
                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty(HEADER_USER_AGENT_KEY, HEADER_USER_AGNET_VALUE);
            } else {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.addRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            // 获取URLConnection对象对应的输出流,隐含性的打开连接
            dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer postBuf = new StringBuffer();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                postBuf.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                postBuf.append("=");
                postBuf.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                postBuf.append("&");
            }
            if (postBuf.length() > 0) {
                postBuf.deleteCharAt(postBuf.length() - 1);
            }
            dos.write(postBuf.toString().getBytes("UTF-8"));
            // flush输出流的缓冲
            dos.flush();

            // 获取返回内容长度，单位字节
            int length = conn.getContentLength();
            // 获取代码返回值
            int respondCode = conn.getResponseCode();

            StringBuffer buff = length > 0 ? new StringBuffer(length) : new StringBuffer();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                buff.append("\n");
                buff.append(line);
            }

            HttpResponse r = new HttpResponse();
            r.setContent(buff.toString());
            r.setContentEncoding(conn.getContentEncoding());
            r.setContentType(conn.getContentType());
            r.setHttpCode(respondCode);
            return r;
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                //ignored
                ex.printStackTrace();
            }
        }
    }

    /**
     * http请求的返回值,包含一般关心的内容
     */
    static class HttpResponse {

        int httpCode;
        String content;
        String contentType;
        String contentEncoding;

        public String getContentEncoding() {
            return contentEncoding;
        }

        public void setContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getHttpCode() {
            return httpCode;
        }

        public void setHttpCode(int httpCode) {
            this.httpCode = httpCode;
        }


    }
}
