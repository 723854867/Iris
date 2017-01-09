package com.busap.vcs.data.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频状态
 * @author meizhiwen
 *
 */
public enum VideoStatus {

	新建("init","新建"),
	上传成功("upload_ok","上传成功"),
	上传失败("upload_err","上传失败"),
	获取媒体信息成功("meta_ok","获取媒体信息成功"),
	获取媒体信息失败("meta_err","获取媒体信息失败"),
	截图成功("capture_ok","截图成功"),
	截图失败("capture_err","截图失败"),
	转码成功("transcode_ok","转码成功"),
	转码失败("transcode_err","转码失败"),
	未审核("uncheck","未审核"),
	审核通过("check_ok","审核通过"),
	审核未通过("check_fail","审核未通过"),
	已删除("delete","已删除"),
	已发布("published","已发布"),
	计划发布("planPublish","计划发布");
	
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

	private static Map<String,VideoStatus> nameMap;
	
	private static Map<String,VideoStatus> descMap;
	
	static{
		nameMap=new HashMap<String,VideoStatus>();
		descMap=new HashMap<String,VideoStatus>();
		
		for (VideoStatus vs : VideoStatus.values()) {
			nameMap.put(vs.name, vs);
			descMap.put(vs.description, vs);
		}
	}
	
	private VideoStatus(String name,String description){
		this.name=name;
		this.description=description;
	}
	
	public static VideoStatus getVideoStatusByName(String name){
		return nameMap.get(name);
	}
	
	public static VideoStatus getVideoStatusByDesc(String description){
		return descMap.get(description);
	}
}
