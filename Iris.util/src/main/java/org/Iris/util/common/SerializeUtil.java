package org.Iris.util.common;

import com.google.gson.Gson;

public interface SerializeUtil {

	interface JsonUtil {
		final Gson GSON = new Gson();
	}
	
	final String OK = "ok";
}
