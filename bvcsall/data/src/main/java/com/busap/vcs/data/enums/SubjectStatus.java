package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 专题状态
 * @author meizhiwen
 *
 */
public enum SubjectStatus {

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

	private static Map<String,SubjectStatus> nameMap;
	
	private static Map<String,SubjectStatus> descMap;
	
	static{
		nameMap=new HashMap<String,SubjectStatus>();
		descMap=new HashMap<String,SubjectStatus>();
		
		for (SubjectStatus vs : SubjectStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private SubjectStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static SubjectStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static SubjectStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
