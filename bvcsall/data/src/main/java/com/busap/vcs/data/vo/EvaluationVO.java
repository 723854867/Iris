package com.busap.vcs.data.vo;

/**
 * 评论展示类
 * @author dmsong
 * @create_at 20150126
 *
 */
public class EvaluationVO extends BaseVO {

	private Long videoId; //视频id
	
	private String content; //评价类容  
	
	private Long pid; //被回复人id
	
	private String creatorName; //创建人名称
	
	private String videoName;	//视频名称
	
	private String pName;		//被回复人名称

	private String username;

	private String phone;

	
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

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
