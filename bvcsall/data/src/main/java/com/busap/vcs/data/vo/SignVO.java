package com.busap.vcs.data.vo;

public class SignVO extends BaseVO {
	private Integer signNum;//积分数量
	private Integer continueSign;//连续获取天数
	private String dateMark;//日期标示
	private Integer type;//获取方式
	private Integer allNum;//每天获得积分的用户数
	private Integer allSignNum;//获得的积分总数
	private Integer beyongMaxNum;//大于30天的用户
	private Integer lessMaxNum;//30天以内的用户
	private Integer isgetSign;//是否已经签到获取积分 0未，1获取
	private Integer userAllSignNum;//用户的总积分数
	private Integer nextSignNum;//下次签到能获取到的积分
	private Integer videoId;//分享点赞获取积分的视频id
	private Long fromUid;//分享点赞获取积分的积分获取来源
	public Integer getSignNum() {
		return signNum;
	}
	public void setSignNum(Integer signNum) {
		this.signNum = signNum;
	}
	public Integer getContinueSign() {
		return continueSign;
	}
	public void setContinueSign(Integer continueSign) {
		this.continueSign = continueSign;
	}
	public String getDateMark() {
		return dateMark;
	}
	public void setDateMark(String dateMark) {
		this.dateMark = dateMark;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getAllNum() {
		return allNum;
	}
	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}
	public Integer getAllSignNum() {
		return allSignNum;
	}
	public void setAllSignNum(Integer allSignNum) {
		this.allSignNum = allSignNum;
	}
	public Integer getBeyongMaxNum() {
		return beyongMaxNum;
	}
	public void setBeyongMaxNum(Integer beyongMaxNum) {
		this.beyongMaxNum = beyongMaxNum;
	}
	public Integer getLessMaxNum() {
		return lessMaxNum;
	}
	public void setLessMaxNum(Integer lessMaxNum) {
		this.lessMaxNum = lessMaxNum;
	}
	public Integer getIsgetSign() {
		return isgetSign;
	}
	public void setIsgetSign(Integer isgetSign) {
		this.isgetSign = isgetSign;
	}
	public Integer getUserAllSignNum() {
		return userAllSignNum;
	}
	public void setUserAllSignNum(Integer userAllSignNum) {
		this.userAllSignNum = userAllSignNum;
	}
	public Integer getNextSignNum() {
		return nextSignNum;
	}
	public void setNextSignNum(Integer nextSignNum) {
		this.nextSignNum = nextSignNum;
	}
	public Integer getVideoId() {
		return videoId;
	}
	public void setVideoId(Integer videoId) {
		this.videoId = videoId;
	}
	public Long getFromUid() {
		return fromUid;
	}
	public void setFromUid(Long fromUid) {
		this.fromUid = fromUid;
	}
	
	
}
