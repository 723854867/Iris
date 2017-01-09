package com.busap.vcs.data.vo;

public class SysmessVO extends BaseVO {
	private String title;
	private String content;
	private String platform;
	private String destUser;
	private String publishTime;
	private String stat;
    private String operation;		//点击消息操作，app 打开应用首页、video打开视频详情页、activity打开活动详情页
	private Long targetid;			//操作目标对象id，视频id或者活动id

	/* 消息图片 */
	private String imagePath;

	private String targetUrl;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDestUser() {
		return destUser;
	}
	public void setDestUser(String destUser) {
		this.destUser = destUser;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Long getTargetid() {
		return targetid;
	}
	public void setTargetid(Long targetid) {
		this.targetid = targetid;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
}
