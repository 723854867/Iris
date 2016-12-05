package org.Iris.util.network.http.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.util.EntityUtils;

public abstract class SyncRespHandler<T> implements org.apache.http.client.ResponseHandler<T> {

	@Override
	public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		StatusLine statusLine = response.getStatusLine();
		HttpEntity entity = response.getEntity();
		try {
			if (statusLine.getStatusCode() >= 300) 
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase() + " --- " + EntityUtils.toString(entity));
			if (null == entity)
				throw new ClientProtocolException("Response contains no content");
			
			T result = parseResult(entity);
			return result;
		} finally {
			EntityUtils.consume(entity);
		}
	}
	
	protected abstract T parseResult(HttpEntity entity) throws UnsupportedOperationException, IOException; 
}

