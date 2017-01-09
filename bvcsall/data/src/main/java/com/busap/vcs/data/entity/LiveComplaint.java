package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//用户投诉
@Entity
@Table(name = "live_complaint")
public class LiveComplaint extends BaseEntity{  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5008354290006791122L;

	
	private String  liveTitle;    //直播名称
	
	private Long  liveId; 	//直播id
	
	private String  content;	//投诉内容
	
	@Column(name = "stat",columnDefinition = "int(4) NOT NULL DEFAULT 0",nullable=true)
	private Integer  stat;//投诉处理状态，默认0 未处理，1 已处理
	
	private String liveImg;		//直播封面
	
	private Long complaintsId;
	
	private String complaints;
	
	private Long liveUserId;
	
	private String liveUserName;

	public String getLiveTitle() {
		return liveTitle;
	}

	public void setLiveTitle(String liveTitle) {
		this.liveTitle = liveTitle;
	}

	public Long getLiveId() {
		return liveId;
	}

	public void setLiveId(Long liveId) {
		this.liveId = liveId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public String getLiveImg() {
		return liveImg;
	}

	public void setLiveImg(String liveImg) {
		this.liveImg = liveImg;
	}

	public Long getComplaintsId() {
		return complaintsId;
	}

	public void setComplaintsId(Long complaintsId) {
		this.complaintsId = complaintsId;
	}

	public String getComplaints() {
		return complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public Long getLiveUserId() {
		return liveUserId;
	}

	public void setLiveUserId(Long liveUserId) {
		this.liveUserId = liveUserId;
	}

	public String getLiveUserName() {
		return liveUserName;
	}

	public void setLiveUserName(String liveUserName) {
		this.liveUserName = liveUserName;
	}
	
	


	
}
