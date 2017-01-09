package com.busap.vcs.data.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "variety_history")
public class VarietyHistory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5673888272000996580L;
	
	private String name;	//期数名称
	private String uids;	//多个用户id用逗号（,）分割
	
	private String tag;		//标签
	
	private String playUrl;	//视频播放地址，短视频模式
	private String picUrl;	//图片地址
	
	@Transient
	private List<Ruser> users;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUids() {
		return uids;
	}
	public void setUids(String uids) {
		this.uids = uids;
	}
	
	public String getPlayUrl() {
		return playUrl;
	}
	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public List<Ruser> getUsers() {
		return users;
	}
	public void setUsers(List<Ruser> users) {
		this.users = users;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
