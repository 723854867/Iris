package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限状态
 * @author meizhiwen
 *
 */
public enum PermissionStatus {

	可用("valid","可用"),
	不可用("invalid","不可用");
	
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

	private static Map<String,PermissionStatus> nameMap;
	
	private static Map<String,PermissionStatus> descMap;
	
	static{
		nameMap=new HashMap<String,PermissionStatus>();
		descMap=new HashMap<String,PermissionStatus>();
		
		for (PermissionStatus vs : PermissionStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private PermissionStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static PermissionStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static PermissionStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
