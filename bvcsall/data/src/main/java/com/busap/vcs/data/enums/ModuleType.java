package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 标签状态
 * @author meizhiwen
 *
 */
public enum ModuleType {

	移动麦视后台("myvideo_restadmin","移动麦视后台"),
	麦视网站后台("myvideo_admin","麦视网站后台"),
	麦视网站("myvideo_www","麦视网站"),
	麦视rest接口("myvideo_restwww","麦视rest接口");
	
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

	private static Map<String,ModuleType> nameMap;
	
	private static Map<String,ModuleType> descMap;
	
	static{
		nameMap=new HashMap<String,ModuleType>();
		descMap=new HashMap<String,ModuleType>();
		
		for (ModuleType vs : ModuleType.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private ModuleType(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static ModuleType getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static ModuleType getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
