package com.busap.vcs.data.vo;


public class ComplainVO extends BaseVO {
	
	private String  title;    
	
	private String  videoId; 
	
	private String  content;
	
	private Integer  stat;//投诉处理状态，默认0 未处理，1 已处理（取消）
	
	private String playKey;
	
	private String videoName;
	
	private String videoCreator;
	
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

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public String getPlayKey() {
		return playKey;
	}

	public void setPlayKey(String playKey) {
		this.playKey = playKey;
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
	
	
}
