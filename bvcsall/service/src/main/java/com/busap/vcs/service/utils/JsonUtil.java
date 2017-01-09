package com.busap.vcs.service.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;



/**
 * Json格式处理类
 */
public  class JsonUtil {
	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/** 私有构造器 **/
	private JsonUtil() {
	}

	/**
	 * 将Object对象转换成Json
	 * 
	 * @param object
	 *            Object对象
	 * @return Json字符串
	 * @throws java.io.IOException
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static String convertObject2Json(Object object) throws JsonGenerationException, JsonMappingException, IOException {
			return objectMapper.writeValueAsString(object);
	}

	/**
	 * 将Json转换成Object对象
	 * 
	 * @param json
	 *            Json字符串
	 * @param cls
	 *            转换成的对象类型
	 * @return 转换之后的对象
	 * @throws java.io.IOException
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T convertJson2Object(String json, Class<T> cls) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(json, cls);
	}
}
