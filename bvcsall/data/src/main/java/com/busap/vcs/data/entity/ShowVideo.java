package com.busap.vcs.data.entity;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

//我拍秀
@Entity
@Table(name = "show_video")
public class ShowVideo extends BaseEntity {

	private static final long serialVersionUID = -2256530130558600444L;
	
	@Column(name = "title",columnDefinition = "varchar(50) NOT NULL ",nullable=false)
	private String title;		//标题
	@Column(name = "description",columnDefinition = "varchar(200) NOT NULL ",nullable=false)
	private String description;		//描述
	@Column(name = "pic",columnDefinition = "varchar(200) NOT NULL ",nullable=false)
	private String pic;		//图片地址
	@Column(name = "video_id",nullable=true)
	private Long videoId;	//视频id
	@Column(name = "ref_video_id",columnDefinition = "varchar(200) NOT NULL ",nullable=true)
	private String refVideoId;		//关联视频id ,多个id用逗号分隔
	@Column(name = "ref_user_id",columnDefinition = "varchar(200) NOT NULL ",nullable=true)
	private String refUserId;		//关联视频的上传用户id，多个id用逗号分隔
	
	@Transient
	private List<Map<String,String>> userList;  //关联视频的上传用户列表
	
	@Transient
	private Integer playCount;   //我拍秀视频的播放次数
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public String getRefVideoId() {
		return refVideoId;
	}
	public void setRefVideoId(String refVideoId) {
		this.refVideoId = refVideoId;
	}
	public String getRefUserId() {
		return refUserId;
	}
	public void setRefUserId(String refUserId) {
		this.refUserId = refUserId;
	}
	public Integer getPlayCount() {
		return playCount;
	}
	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}
	public List<Map<String, String>> getUserList() {
		return userList;
	}
	public void setUserList(List<Map<String, String>> userList) {
		this.userList = userList;
	}
	
	
}
