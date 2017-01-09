package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限状态
 * @author meizhiwen
 *
 */
public enum SystemType {

	系统菜单("BVCS","系统菜单"),
	SHIRO("Security","SHIRO");
	
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

	private static Map<String,SystemType> nameMap;
	
	private static Map<String,SystemType> descMap;
	
	static{
		nameMap=new HashMap<String,SystemType>();
		descMap=new HashMap<String,SystemType>();
		
		for (SystemType vs : SystemType.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private SystemType(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static SystemType getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static SystemType getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
