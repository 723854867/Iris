package org.Iris.util.network.http.handler;

import java.io.IOException;
import java.nio.charset.Charset;

import org.Iris.util.common.SerializeUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

public class SyncXmlRespHandler<T> extends SyncRespHandler<T>{
	private Class<T> clazz;
	
	public SyncXmlRespHandler(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}
	
	@SuppressWarnings("all")
	@Override
	protected T parseResult(HttpEntity entity) throws UnsupportedOperationException, IOException {
		ContentType contentType = ContentType.getOrDefault(entity);
		Charset charset = contentType.getCharset();
		if (null == charset)
			charset = Charset.forName("utf-8");
		SerializeUtil.XmlUtil.xmlToBean(entity.getContent().toString(), clazz);
		return null;
	}
	
	public static <T> SyncXmlRespHandler<T> build(Class<T> clazz) { 
		return new SyncXmlRespHandler<T>(clazz);
	}
}
