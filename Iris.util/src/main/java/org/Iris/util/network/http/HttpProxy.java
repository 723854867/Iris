package org.Iris.util.network.http;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.Iris.util.concurrent.DefaultThreadFactory;
import org.Iris.util.network.http.handler.AsyncRespHandler;
import org.Iris.util.network.http.handler.SyncRespHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpProxy {

	private AsyncHttpAdapter asyncHttp;
	private SyncHttpAdapter syncHttp;

	private ScheduledExecutorService executorService;
	private ScheduledFuture<?> future;

	public void init() throws Exception {
		if (null != this.asyncHttp)
			this.asyncHttp.init();
		if (null != this.syncHttp)
			this.syncHttp.init();

		this.executorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(HttpProxy.class));
		this.future = this.executorService.scheduleWithFixedDelay(new Runnable() {
			public void run() {
				if (null != asyncHttp)
					asyncHttp.idleExpireClear();
				if (null != syncHttp)
					syncHttp.idleExpireClear();
			}
		}, 5, 5, TimeUnit.SECONDS);
	}

	public void asyncRequest(HttpUriRequest request, AsyncRespHandler respHandler) {
		this.asyncHttp.execute(request, respHandler);
	}

	public <T> T syncRequest(HttpUriRequest request, SyncRespHandler<T> respHandler)
			throws ClientProtocolException, IOException {
		return this.syncHttp.execute(request, respHandler);
	}

	public void setAsyncHttp(AsyncHttpAdapter asyncHttp) {
		this.asyncHttp = asyncHttp;
	}

	public void setSyncHttp(SyncHttpAdapter syncHttp) {
		this.syncHttp = syncHttp;
	}

	public void close() {
		this.executorService.shutdownNow();
		this.future.cancel(true);
		if (null != this.syncHttp)
			this.syncHttp.close();
		if (null != this.asyncHttp)
			this.asyncHttp.close();
		this.executorService = null;
		this.future = null;
		this.syncHttp = null;
		this.asyncHttp = null;
	}
}
