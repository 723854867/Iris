package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据来源
 * @author meizhiwen
 *
 */
public enum Platform {
	IOS("ios"),
	ANDROID("android"),
	ALL("all");
	
	private String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private static Map<String,Platform> nameMap;
	
	static{
		nameMap=new HashMap<String,Platform>();
		
		for (Platform vs : Platform.values()) {
			nameMap.put(vs.name, vs);
		}
	}
	
	private Platform(String name){
		this.name=name;
	}
	
	public static Platform getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
}
