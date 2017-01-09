package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * 赞
 */
@Entity
@Table(name = "praise")
public class Praise extends BaseEntity{
 
	private static final long serialVersionUID = -1569866178841288146L;

	private Long videoId; //视频id 
	
	private Long adminId; //后台操作人id

	private Integer status = 0;
	
	@Transient
	private Video video;

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
