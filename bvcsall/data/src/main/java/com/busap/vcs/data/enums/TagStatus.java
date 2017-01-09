package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签状态
 * @author meizhiwen
 *
 */
public enum TagStatus {

	活动("active","活动"),
	推荐("recommend","推荐");
	
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

	private static Map<String,TagStatus> nameMap;
	
	private static Map<String,TagStatus> descMap;
	
	static{
		nameMap=new HashMap<String,TagStatus>();
		descMap=new HashMap<String,TagStatus>();
		
		for (TagStatus vs : TagStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private TagStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static TagStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static TagStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
