package org.Iris.util.network.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.Iris.util.concurrent.DefaultThreadFactory;
import org.Iris.util.network.http.handler.AsyncRespHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.conn.NoopIOSessionStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

public class AsyncHttpAdapter extends HttpAdapter {

	private int workerCount = 10;
	private int connectTimeout = 3000;
	private int soTimeout = 3000;
	private boolean tcpNoDelay = true;
	private boolean keepAlive = true;
	private CloseableHttpAsyncClient httpClients;
	private PoolingNHttpClientConnectionManager cm;

	@Override
	void init() throws Exception {
		HttpAsyncClientBuilder builder = HttpAsyncClients.custom();
		
		// 设置连接池
		this.cm = connManager();
		builder.setConnectionManager(this.cm);
		// 设置keep alive
		builder.setKeepAliveStrategy(keepAliveStrategy());
		
		this.httpClients = builder.build();
		this.httpClients.start();
	}

	@Override
	void idleExpireClear() {
		this.cm.closeExpiredConnections();
		this.cm.closeIdleConnections(30, TimeUnit.SECONDS);
	}
	
	@Override
	void close() {
		try {
			this.cm.shutdown();
			this.httpClients.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cm = null;
		this.httpClients = null;
	}
	
	private PoolingNHttpClientConnectionManager connManager() throws IOReactorException {
		IOReactorConfig reactorConfig = IOReactorConfig.custom()
				.setIoThreadCount(this.workerCount)
				.setConnectTimeout(this.connectTimeout)
				.setSoTimeout(this.soTimeout)
				.setTcpNoDelay(this.tcpNoDelay)
				.setSoKeepAlive(this.keepAlive)
				.build();
		ConnectingIOReactor reactor = new DefaultConnectingIOReactor(reactorConfig, new DefaultThreadFactory(AsyncHttpAdapter.class));
		
		RegistryBuilder<SchemeIOSessionStrategy> builder = RegistryBuilder.<SchemeIOSessionStrategy>create();
		builder.register("http", NoopIOSessionStrategy.INSTANCE);
		if (sslEnabled)
			builder.register("https", SSLIOSessionStrategy.getDefaultStrategy());
		PoolingNHttpClientConnectionManager cm = new PoolingNHttpClientConnectionManager(reactor, builder.build());
		cm.setMaxTotal(this.maxConn);
		cm.setDefaultMaxPerRoute(this.maxConnPerRoute);
		return cm;
	}
	
	public void execute(HttpUriRequest request, AsyncRespHandler respHandler) {
		this.httpClients.execute(request, respHandler);
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public void setWorkerCount(int workerCount) {
		this.workerCount = workerCount;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
}
