package org.Iris.util.network.email;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.Iris.util.common.SerializeUtil;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.SyncHttpAdapter;
import org.Iris.util.network.http.handler.SyncStrRespHandler;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

public class HttpProxyTest {

	public static void main(String[] args) throws Exception {
		HttpClientBuilder builder = HttpClients.custom();
		PlainConnectionSocketFactory psf = PlainConnectionSocketFactory.getSocketFactory();
		RegistryBuilder<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create().register("http", psf);
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			@Override
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		r.register("https", sslsf);
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r.build());
		cm.setMaxTotal(100);								// 设置总的最大连接数
		cm.setDefaultMaxPerRoute(100);				// 设置每个路由的最大连接
		// 设置连接池
		builder.setConnectionManager(cm);
		// 设置keep alive 策略
		builder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
			public long getKeepAliveDuration(HttpResponse response, org.apache.http.protocol.HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (-1 == keepAlive)
					keepAlive = 5000; 
				return keepAlive;
			}
		});
		// 设置失败重发handler
		builder.setRetryHandler(new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= 3)
					return false;
				if (exception instanceof InterruptedIOException)
					return false;
				if (exception instanceof UnknownHostException)
					return false;
				if (exception instanceof ConnectTimeoutException)
					return false;
				if (exception instanceof SSLException)
					return false;
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				org.apache.http.HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) 
					return true;
				return false;
			}
		});
		CloseableHttpClient httpClient = builder.build();
//				
//		HttpPost post = new HttpPost();
//        List<NameValuePair> qparams = new ArrayList<NameValuePair>();  
//        qparams.add(new BasicNameValuePair("_eventId", "submit"));  
//        qparams.add(new BasicNameValuePair("systemCode", "motorui"));  
//        qparams.add(new BasicNameValuePair("usernameDisplay", "Z0015"));  
//        qparams.add(new BasicNameValuePair("username", "Z0015"));  
//        qparams.add(new BasicNameValuePair("password", "L6IyIs1X7"));  
//        qparams.add(new BasicNameValuePair("lt", "_cD930FC2E-E6E6-7C14-FD30-ED068CC535F9_k94311C99-CE86-6170-3A6F-B7B4C9B20A1B"));
//        //设定需要访问的URL，第四个参数为表单提交路径  
//        URIBuilder uriBuilder = new URIBuilder("https://sso.libertymutual.com.cn/casserver/login?systemCode=motorui&service=https://lm.libertymutual.com.cn/insure/insure-ui/shiro-cas.do");
//        uriBuilder.addParameters(qparams);
//        System.out.println(uriBuilder.build());
//        //将参数加入URL中  
//        //Post提交  
//        HttpPost httpPost = new HttpPost(uriBuilder.build());  
//        HttpResponse response = httpClient.execute(httpPost);
//        StatusLine statusLine = response.getStatusLine();
//		HttpEntity entity = response.getEntity();
//        int status = statusLine.getStatusCode();
//		if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) {
//			Header header = response.getFirstHeader(HttpHeaders.LOCATION);
//			String url = header.getValue();
//			System.out.println(url);
//		}
//		System.out.println(EntityUtils.toString(entity));
		HttpPost post = new HttpPost("https://sso.libertymutual.com.cn/casserver/login?systemCode=motorui&service=https://lm.libertymutual.com.cn/insure/insure-ui/shiro-cas.do");
		post.setHeader("Cookie", "JSESSIONID=A653696B587DC5AC57BA6A857BEB9C96; SESSION=bd0ebc01-1663-4885-b30d-f77690bb4dd2");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryType", 9);
		map.put("documentNo", "");
		map.put("plateNo", "浙AH0155R");
		map.put("riskcode", "0502");
		map.put("pageSizeForDBQuery", 10);
		map.put("pageNo", 1);
		String json = SerializeUtil.JsonUtil.GSON.toJson(map);
		System.out.println(json);
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
		HttpResponse response = httpClient.execute(post);
		int status = response.getStatusLine().getStatusCode();
		System.out.println(response.getStatusLine().getStatusCode());
		if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) {
			Header header = response.getFirstHeader(HttpHeaders.LOCATION);
			String url = header.getValue();
			System.out.println(url);
		}
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}
}
