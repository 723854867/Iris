package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签状态
 * @author meizhiwen
 *
 */
public enum HttpRequestType {

	get("get","get"),
	post("post","post"),
	delete("delete","delete"),
	put("put","put");
	
	private String name;
	
	private String description;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private static Map<String,HttpRequestType> nameMap;
	
	private static Map<String,HttpRequestType> descMap;
	
	static{
		nameMap=new HashMap<String,HttpRequestType>();
		descMap=new HashMap<String,HttpRequestType>();
		
		for (HttpRequestType vs : HttpRequestType.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private HttpRequestType(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static HttpRequestType getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static HttpRequestType getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
