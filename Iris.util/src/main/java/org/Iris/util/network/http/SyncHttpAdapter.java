package org.Iris.util.network.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.Iris.util.network.http.handler.SyncRespHandler;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncHttpAdapter extends HttpAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(SyncHttpAdapter.class);
	
	private CloseableHttpClient httpClients;
	private PoolingHttpClientConnectionManager cm;
	private int retryCount = 3;
	
	@Override
	void init() throws Exception { 
		HttpClientBuilder builder = HttpClients.custom();
		
		// 设置连接池
		this.cm = connManager();
		builder.setConnectionManager(this.cm);
		// 设置keep alive 策略
		builder.setKeepAliveStrategy(keepAliveStrategy());
		// 设置失败重发handler
		builder.setRetryHandler(retryHandler());
		this.httpClients = builder.build();
	}
	
	void idleExpireClear() {
		cm.closeExpiredConnections();
		cm.closeIdleConnections(30, TimeUnit.SECONDS);
	}
	
	/**
	 * 设置http连接池
	 * 
	 * @return
	 */
	private PoolingHttpClientConnectionManager connManager() throws Exception { 
		PlainConnectionSocketFactory psf = PlainConnectionSocketFactory.getSocketFactory();
		RegistryBuilder<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create().register("http", psf);
		if (sslEnabled) {			// 如果支持ssl，则需要配置ssl套接字工厂
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			r.register("https", sslsf);
		}
		
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r.build());
		cm.setMaxTotal(this.maxConn);								// 设置总的最大连接数
		cm.setDefaultMaxPerRoute(this.maxConnPerRoute);				// 设置每个路由的最大连接
		return cm;
	}
	
	/**
	 * 设置失败重连次数
	 * 
	 * @return
	 */
	private HttpRequestRetryHandler retryHandler() {
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
				if (executionCount >= SyncHttpAdapter.this.retryCount)
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
				if (idempotent) {
					logger.info("{} retry, total retry count : {}.", request.toString(), executionCount);
					return true;
				}
				return false;
			}
		};
		return retryHandler;
	}
	
	@Override
	public void close() { 
		try {
			this.cm.close();
			this.httpClients.close();
		} catch (IOException e) {
			logger.warn("HttpClients close exception {}.", e);
		}
		this.cm = null;
		this.httpClients = null;
	}
	
	public <T> T execute(HttpUriRequest request, SyncRespHandler<T> responseHandler) throws ClientProtocolException, IOException {
		return httpClients.execute(request, responseHandler);
	}
	
	HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return httpClients.execute(request);
	}
	
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}
}
//public static HttpContext localContext = new BasicHttpContext();
//public static HttpClientContext context = HttpClientContext.adapt(localContext);
//public void session() throws Exception { 
//	CloseableHttpClient httpClient = HttpClients.createDefault();
//	
//	List<NameValuePair> params = new ArrayList<NameValuePair>();
//	params.add(new BasicNameValuePair("username", "**"));
//	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
//	HttpPost httpPost = new HttpPost("http://localhost/test/login");
//	httpPost.setEntity(entity);
//	CloseableHttpResponse response = httpClient.execute(httpPost, context);
//	
//	HttpGet httpGet = new HttpGet("http://localhost/test/login");
//	response = httpClient.execute(httpGet, context);
//	// 在后台login成功会把信息写入到session中，请注意浏览器会自动帮我们发送sessionId，但是
//	// httpclient可没有这么智能，那么怎样才能实现不同的请求之间是相关联的呢，那就是用httpcontext；
//	// BasicHttpContext里有个Map<Object,Object>的对象用来记录一次请求响应的信息，当响应信息返回时，
//	// 就会被set到context里，当然响应的cookie信息也就被存储在context里,包括传回的sessionId
//	// 当第二次请求的时候传入相同的context，那么请求的过程中会将context里的sessionId提取出来传给服务器
//}
//
//public void requestInterceptor() throws ClientProtocolException, IOException { 
//	CloseableHttpClient httpClient = HttpClients.custom().addInterceptorLast(new HttpRequestInterceptor() {
//		@Override
//		public void process(org.apache.http.HttpRequest request, HttpContext context) throws HttpException, IOException {
//			AtomicInteger count = (AtomicInteger) context.getAttribute("count");
//			request.addHeader("Count", Integer.toString(count.getAndIncrement()));
//		}
//	}).build();
//	AtomicInteger count = new AtomicInteger(1);
//	HttpClientContext localContext = HttpClientContext.create();
//	localContext.setAttribute("count", count);
//	
//	HttpGet httpGet = new HttpGet("http://localhost/");
//	for (int i = 0; i < 10; i++) {
//		CloseableHttpResponse response = httpClient.execute(httpGet, localContext);
//		HttpEntity entity = response.getEntity();
//	}
//}