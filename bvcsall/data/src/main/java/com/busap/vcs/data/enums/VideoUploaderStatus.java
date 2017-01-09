package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 上传者状态
 * @author meizhiwen
 *
 */
public enum VideoUploaderStatus {

	活动("active","活动"),
	非活动("unactive","非活动");
	
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

	private static Map<String,VideoUploaderStatus> nameMap;
	
	private static Map<String,VideoUploaderStatus> descMap;
	
	static{
		nameMap=new HashMap<String,VideoUploaderStatus>();
		descMap=new HashMap<String,VideoUploaderStatus>();
		
		for (VideoUploaderStatus vs : VideoUploaderStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private VideoUploaderStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static VideoUploaderStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static VideoUploaderStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
