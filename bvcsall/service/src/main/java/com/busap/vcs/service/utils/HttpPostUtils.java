package com.busap.vcs.service.utils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BufferedHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求工具类
 * User: huoshanwei
 * Date: 16-04-22
 * Time: 上午16:57
 */
public class HttpPostUtils {

    public static String doHttpPost(String postUrl, Map<String, String> params) {

        HttpClient client = new HttpClient();
        UTF8PostMethod mPost = new UTF8PostMethod(postUrl);
        client.getParams().setParameter("http.socket.timeout", new Integer(600000));
        String[] keys = params.keySet().toArray(new String[0]);
        for (String key : keys) {
            mPost.addParameter(key, params.get(key));
        }
        //mPost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String responseTxt = "";
        mPost.getResponseCharSet();
        try {
            int responseCode = client.executeMethod(mPost);
            if (responseCode != HttpStatus.SC_OK) {
                responseTxt = "";
            } else {
                responseTxt = mPost.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mPost.releaseConnection();
                client.getHttpConnectionManager().closeIdleConnections(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseTxt;
    }

    public static String doHttpPost(String postUrl, Header header, Map<String, String> params) {

        HttpClient client = new HttpClient();
        UTF8PostMethod mPost = new UTF8PostMethod(postUrl);
        client.getParams().setParameter("http.socket.timeout", new Integer(600000));
        String[] keys = params.keySet().toArray(new String[0]);
        for (String key : keys) {
            mPost.addParameter(key, params.get(key));
        }
        mPost.addRequestHeader(header);
        //mPost.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        String responseTxt = "";
        mPost.getResponseCharSet();
        try {
            int responseCode = client.executeMethod(mPost);
            System.out.println(responseCode);
            System.out.println(mPost.getResponseHeader("X-Reqid"));
            if (responseCode != HttpStatus.SC_OK) {
                responseTxt = "";
            } else {
                responseTxt = mPost.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mPost.releaseConnection();
                client.getHttpConnectionManager().closeIdleConnections(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseTxt;
    }

    static class UTF8PostMethod extends PostMethod {

        public UTF8PostMethod(String url) {
            super(url);
        }

        @Override
        public String getRequestCharSet() {
            return "UTF-8";
        }
    }

    public static String doHttpPostSubmit(String loginUrl, String postUrl, Map<String, String> params) {

        HttpClient client = new HttpClient();
        UTF8PostMethod login = new UTF8PostMethod(loginUrl);
        UTF8PostMethod mPost = new UTF8PostMethod(postUrl);
        client.getParams().setParameter("http.socket.timeout", new Integer(600000));
        String responseTxt = "";
        int responseCode = 0;
        try {
            //登录
            login.addParameter("username", "admin");
            login.addParameter("password", "admin");
            login.getResponseCharSet();
            responseCode = client.executeMethod(login);
            //提交
            String[] keys = params.keySet().toArray(new String[0]);
            for (String key : keys) {
                mPost.addParameter(key, params.get(key));
            }
            mPost.getResponseCharSet();

            responseCode = client.executeMethod(mPost);
            if (responseCode != HttpStatus.SC_OK) {
                responseTxt = "";
            } else {
                responseTxt = mPost.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mPost.releaseConnection();
                client.getHttpConnectionManager().closeIdleConnections(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseTxt;
    }


    public static Map<String,String> httpPostStringEntity(String url, Map<String, String> headMap, String body) {
        Map<String,String> resultMap = new HashMap<String, String>();
        String response = "";
        HttpPost httpPost = null;
        CloseableHttpResponse ht = null;
        String statusCode = "-";
        String contentLength = "-";
        String contentType = "-";
        try {
            httpPost = new HttpPost(url);

            StringEntity s =  new StringEntity(body, "utf-8");
            httpPost.setEntity(s);

            if (headMap != null && headMap.size() > 0) {
                for (String name : headMap.keySet()) {
                    httpPost.addHeader(name, headMap.get(name));
                }
            }
            if(!httpPost.containsHeader("User-Agent"))
                httpPost.addHeader("User-Agent", "busonline-live");
            CloseableHttpClient hc = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();//设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            ht = hc.execute(httpPost);

            org.apache.http.Header[] headerArr = ht.getAllHeaders();
            for (org.apache.http.Header header : headerArr) {
                BufferedHeader bh = (BufferedHeader) header;
                if (bh.getBuffer().toString().contains("Content-Length")) {
                    contentLength = bh.getValue();
                } else if (bh.getBuffer().toString().contains("Content-Type")) {
                    contentType = bh.getValue();
                }
            }
            HttpEntity het = ht.getEntity();
            InputStream is = het.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf8"));
            String readLine;
            while ((readLine = br.readLine()) != null) {
                response = response + readLine;
            }
            is.close();
            br.close();
            int status = ht.getStatusLine().getStatusCode();
            resultMap.put("code", String.valueOf(status));
            resultMap.put("response",response);
            return resultMap;
        } catch (Exception e) {
            statusCode = "500";
            resultMap.put("code", statusCode);
            resultMap.put("response",response);
            return resultMap;
        } finally {
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
            if (ht != null) {
                try {
                    ht.close();
                } catch (IOException ignored) {
                }
            }
        }

    }


}
