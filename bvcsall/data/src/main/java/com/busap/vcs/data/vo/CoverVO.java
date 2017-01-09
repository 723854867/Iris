package com.busap.vcs.data.vo;



public class CoverVO {
	
	private Long id;
	private String title;
	private String startTime;
	private String endTime;
	private int status;
	private String imgBasePath;
	private String redirectUrl;//跳转地址
	private String operation;		//点击消息操作，user 打开个人中心、video打开视频详情页、activity打开活动详情页、web跳转到指定url
	private Long targetid;			//操作目标对象id，视频id或者活动id
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImgBasePath() {
		return imgBasePath;
	}
	public void setImgBasePath(String imgBasePath) {
		this.imgBasePath = imgBasePath;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
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
	
	
	
}	
