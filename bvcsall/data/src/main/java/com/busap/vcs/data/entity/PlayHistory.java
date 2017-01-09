package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
//视频播放记录
@Entity
@Table(name = "play_history")
public class PlayHistory extends BaseEntity{ 
	 
	private static final long serialVersionUID = -4311587438139504652L; 
	
	private Long videoId ;//播放的视频id
	
	private Integer count ;//播放次数
	
	private String deviceId ;//设备id  

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	} 
	
}
