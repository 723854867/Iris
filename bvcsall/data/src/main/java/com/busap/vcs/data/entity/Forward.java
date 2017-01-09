package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//转发
@Entity
@Table(name = "forward")
public class Forward extends BaseEntity {

	private static final long serialVersionUID = -2830256161181204951L;
	
	@Column(name = "video_id",nullable=false)
	private Long videoId;	//被转发的视频id
	
	@Column(name = "author_id",nullable=false)
	private Long authorId;	//被转发的视频作者id
	
	@Column(name = "evaluation",columnDefinition = "varchar(500)",nullable=true)
	private String evaluation;		//转发的评论

	private Integer status = 0;

	private String videoPic;//被转发的视频封面

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getVideoPic() {
		return videoPic;
	}

	public void setVideoPic(String videoPic) {
		this.videoPic = videoPic;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
