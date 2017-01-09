package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼物状态标记
 * @author huoshanwei
 *
 */
public enum GiftMarkerStateEnum {

	NEW("new");

	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private static Map<String,GiftMarkerStateEnum> nameMap;

	static{
		nameMap=new HashMap<String,GiftMarkerStateEnum>();

		for (GiftMarkerStateEnum vs : GiftMarkerStateEnum.values()) {
			nameMap.put(vs.name, vs);
		}
	}

	private GiftMarkerStateEnum(String name){
		this.name=name;
	}
	
	public static GiftMarkerStateEnum getGiftMarkerStateByName(String name){
		return nameMap.get(name);
	}
	
}
