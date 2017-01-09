package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据来源
 * @author meizhiwen
 *
 */
public enum DataFrom {
	移动麦视后台("myvideo_restadmin","移动麦视后台"),
	麦视网站后台("myvideo_admin","麦视网站后台"),
	麦视rest接口("myvideo_restwww","麦视rest接口"),
	麦视网站("myvideo_www","麦视网站");
	
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

	private static Map<String,DataFrom> nameMap;
	
	private static Map<String,DataFrom> descMap;
	
	static{
		nameMap=new HashMap<String,DataFrom>();
		descMap=new HashMap<String,DataFrom>();
		
		for (DataFrom vs : DataFrom.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private DataFrom(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static DataFrom getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static DataFrom getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
