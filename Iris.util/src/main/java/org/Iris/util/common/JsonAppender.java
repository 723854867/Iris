package org.Iris.util.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonAppender {

	private static final Gson GSON = new Gson();
	private static final Gson ANNO_GSON	= new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	
	public static final JsonAppender newAppender() {
		return new JsonAppender();
	}
	
	public static final String toJson(Object obj) {
		return GSON.toJson(obj);
	}
	
	public static final String toAnnoJson(Object obj) {
		return ANNO_GSON.toJson(obj);
	}
	
	private Map<String, String> results;
	
	private JsonAppender() {
		this.results = new HashMap<String, String>();
	}

	public JsonAppender append(String key, String value) {
		this.results.put(key, value);
		return this;
	}
	
	public JsonAppender append(String key, int value) {
		this.results.put(key, value + "");
		return this;
	}
	
	public JsonAppender append(String key, long value) {
		this.results.put(key, value + "");
		return this;
	}
	
	public JsonAppender append(String key, double value) {
		this.results.put(key, value + "");
		return this;
	}
	
	@Override
	public String toString() {
		return GSON.toJson(this.results);
	}
}
