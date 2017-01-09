package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 性别
 * @author meizhiwen
 *
 */
public enum Sex {

	不男不女("2","不男不女"),
	男("1","男"),
	女("0","女");
	
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

	private static Map<String,Sex> nameMap;
	
	private static Map<String,Sex> descMap;
	
	static{
		nameMap=new HashMap<String,Sex>();
		descMap=new HashMap<String,Sex>();
		
		for (Sex vs : Sex.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private Sex(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static Sex getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static Sex getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
