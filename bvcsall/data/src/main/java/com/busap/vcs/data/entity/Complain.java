package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//用户投诉
@Entity
@Table(name = "complain")
public class Complain extends BaseEntity{  
	 
	private static final long serialVersionUID = -8258234756212522540L;
	
	private String  title;    
	
	private String  videoId; 
	
	private String  content;
	@Column(name = "stat",columnDefinition = "int(4) NOT NULL DEFAULT 0",nullable=true)
	private Integer  stat;//投诉处理状态，默认0 未处理，1 已处理（取消）
	
	@Transient
	private String videoImg;
	
	@Transient
	private String videoName;
	
	@Transient
	private String videoCreator;
	
	@Transient
	private String creatorName;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVideoImg() {
		return videoImg;
	}

	public void setVideoImg(String videoImg) {
		this.videoImg = videoImg;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoCreator() {
		return videoCreator;
	}

	public void setVideoCreator(String videoCreator) {
		this.videoCreator = videoCreator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	
}
