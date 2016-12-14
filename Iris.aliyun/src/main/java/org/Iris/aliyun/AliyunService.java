package org.Iris.aliyun;

import java.io.IOException;

import org.Iris.aliyun.exception.AliyunException;
import org.Iris.aliyun.service.AliyunRequest;
import org.Iris.util.network.http.HttpProxy;

public class AliyunService {

	private HttpProxy httpProxy;
	
	public <T> T syncRequest(AliyunRequest request) {
		try {
			httpProxy.syncRequest(request.httpRequest(), null);
			return null;
		} catch (IOException e) {
			throw new AliyunException("Aliyun service request invoke failure!", e);
		}
	}
	
	public void setHttpProxy(HttpProxy httpProxy) {
		this.httpProxy = httpProxy;
	}
}
