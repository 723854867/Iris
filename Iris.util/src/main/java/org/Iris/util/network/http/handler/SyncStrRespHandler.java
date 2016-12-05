package org.Iris.util.network.http.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

public class SyncStrRespHandler extends SyncRespHandler<String> {

	@Override
	protected String parseResult(HttpEntity entity) throws UnsupportedOperationException, IOException {
		return EntityUtils.toString(entity);
	}
}

