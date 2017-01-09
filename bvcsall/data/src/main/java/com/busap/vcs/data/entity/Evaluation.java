package com.busap.vcs.data.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * 评论
 */
@Entity
@Table(name = "evaluation")
public class Evaluation extends BaseEntity{
	 
	private static final long serialVersionUID = -1058107225419680095L;

	private Long videoId; //视频id
	
	private String content; //评价类容  
	
	private Long pid; //被回复人id
	
	private Long adminId;//管理后台添加评论操作人id
	
	@Transient
	private Ruser parent ; //被回复用户信息
	
	@Transient
	private Video video;

	private Integer status = 0;
	
	public Ruser getParent() {
		return parent;
	}

	public void setParent(Ruser parent) {
		this.parent = parent;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
	

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getFormatCreateDateStr() {
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
		if (createDate == null){
			return sdf.format(new Date());
		}else{
			return sdf.format(createDate);
		}
	}
	
}
