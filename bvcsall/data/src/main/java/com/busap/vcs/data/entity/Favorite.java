package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * 收藏
 */
@Entity
@Table(name = "favorite")
public class Favorite extends BaseEntity{
 
	private static final long serialVersionUID = -4086877075363186559L;

	private Long videoId; //视频id 
	
	@Transient
	private Video video;//视频对象
	 

	
	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
 
	
	
}
