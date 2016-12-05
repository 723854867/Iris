package org.Iris.util.network.http.handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.Iris.util.common.SerializeUtil;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

public class SyncJsonRespHandler<T> extends SyncRespHandler<T> {
	
	private Class<T> clazz;
	
	private SyncJsonRespHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected T parseResult(HttpEntity entity) throws UnsupportedOperationException, IOException {
		ContentType contentType = ContentType.getOrDefault(entity);
		Charset charset = contentType.getCharset();
		if (null == charset)
			charset = Charset.forName("utf-8");
		Reader reader = new InputStreamReader(entity.getContent(), charset);
		T instance = SerializeUtil.JsonUtil.GSON.fromJson(reader, clazz);
		return instance;
	}
	
	public static <T> SyncJsonRespHandler<T> build(Class<T> clazz) { 
		return new SyncJsonRespHandler<T>(clazz);
	}
}

