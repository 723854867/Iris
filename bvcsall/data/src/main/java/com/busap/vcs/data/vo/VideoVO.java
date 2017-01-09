package com.busap.vcs.data.vo;

public class VideoVO extends BaseVO {
	private String tag;				//标签
	private String name;			//名称
	private String activity;		//所属活动
	private String flowStat;		//审核状态
	private String isLogo;			//是否贴标
	private String description;		//简介
	private String playKey;			//playkey
	private String creatorName;		//发布者
	private String url;				//视频地址
	private String videoPic;        //新视频截图地址
	
	private Integer praiseCount=0;	//赞数
	private Integer evaluationCount=0;//评论数
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getFlowStat() {
		return flowStat;
	}
	public void setFlowStat(String flowStat) {
		this.flowStat = flowStat;
	}
	public String getIsLogo() {
		return isLogo;
	}
	public void setIsLogo(String isLogo) {
		this.isLogo = isLogo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlayKey() {
		return playKey;
	}
	public void setPlayKey(String playKey) {
		this.playKey = playKey;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getVideoPic() {
		return videoPic;
	}
	public void setVideoPic(String videoPic) {
		this.videoPic = videoPic;
	}
	public Integer getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(Integer praiseCount) {
		this.praiseCount = praiseCount;
	}
	public Integer getEvaluationCount() {
		return evaluationCount;
	}
	public void setEvaluationCount(Integer evaluationCount) {
		this.evaluationCount = evaluationCount;
	}
	
	
}
