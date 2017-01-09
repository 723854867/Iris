package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户类型
 * @author meizhiwen
 *
 */
public enum UserType {

	运营后台("admin","运营后台"),
	马甲("majia","马甲"),
	普通用户("normal","普通用户");
	
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

	private static Map<String,UserType> nameMap;
	
	private static Map<String,UserType> descMap;
	
	static{
		nameMap=new HashMap<String,UserType>();
		descMap=new HashMap<String,UserType>();
		
		for (UserType vs : UserType.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private UserType(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static UserType getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static UserType getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
