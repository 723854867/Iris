package org.Iris.aliyun;

import java.io.IOException;

import org.Iris.aliyun.bean.Format;
import org.Iris.aliyun.exception.AliyunException;
import org.Iris.aliyun.service.AliyunRequest;
import org.Iris.util.network.http.HttpProxy;
import org.Iris.util.network.http.handler.SyncStrRespHandler;

public class AliyunService {

	private HttpProxy httpProxy;
	
	public <T> T syncRequest(AliyunRequest request, Class<T> clazz) {
		Format format = request.getFormat();
		try {
			String result = httpProxy.syncRequest(request.httpRequest(), SyncStrRespHandler.INSTANCE);
			return format.strToBean(result, clazz);
		} catch (IOException e) {
			throw new AliyunException("Aliyun service request invoke failure!", e);
		}
	}
	
	public void setHttpProxy(HttpProxy httpProxy) {
		this.httpProxy = httpProxy;
	}
}
