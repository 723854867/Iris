package org.Iris.util.network.http;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;

public abstract class HttpAdapter {

	protected int keepAliveTime = 5000;
	protected int maxConn = 200;
	protected int maxConnPerRoute = 200;
	protected boolean sslEnabled;

	/**
	 * 设置keepAlive策略;默认为保持5秒
	 * 
	 * @return
	 */
	protected ConnectionKeepAliveStrategy keepAliveStrategy() {
		ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
			public long getKeepAliveDuration(HttpResponse response, org.apache.http.protocol.HttpContext context) {
				long keepAlive = super.getKeepAliveDuration(response, context);
				if (-1 == keepAlive)
					keepAlive = keepAliveTime; // keep connections alive 5
												// seconds if a keep-alive value
												// has not be explicitly set by
												// the server
				return keepAlive;
			}
		};
		return keepAliveStrategy;
	}

	/**
	 * 清理超时和无效链接
	 * 
	 */
	abstract void idleExpireClear();

	abstract void close();

	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	abstract void init() throws Exception;

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public void setMaxConnPerRoute(int maxConnPerRoute) {
		this.maxConnPerRoute = maxConnPerRoute;
	}

	public void setKeepAliveTime(int keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}
}
