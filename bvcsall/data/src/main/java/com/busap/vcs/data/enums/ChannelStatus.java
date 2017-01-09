package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 专题状态
 * @author meizhiwen
 *
 */
public enum ChannelStatus {

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

	private static Map<String,ChannelStatus> nameMap;
	
	private static Map<String,ChannelStatus> descMap;
	
	static{
		nameMap=new HashMap<String,ChannelStatus>();
		descMap=new HashMap<String,ChannelStatus>();
		
		for (ChannelStatus vs : ChannelStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private ChannelStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static ChannelStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static ChannelStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
