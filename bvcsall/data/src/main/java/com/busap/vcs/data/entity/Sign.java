package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sign")
public class Sign extends BaseEntity {

	/**积分记录表
	 * 
	 */
	private static final long serialVersionUID = -2073359218968158690L;
	@Column(name = "sign_num")
	private Integer signNum;//获得积分数量
	@Column(name = "continue_sign")
	private Integer continueSign;//连续获得积分天数
	@Column(name = "date_Mark")
	private String dateMark;//日期标示
	@Column(name = "type")
	private Integer type;//获取方式
	@Column(name = "video_id")
	private Long videoId;//分享点赞获取积分的视频id
	@Column(name = "from_uid")
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
	public Long getVideoId() {
		return videoId;
	}
	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}
	public Long getFromUid() {
		return fromUid;
	}
	public void setFromUid(Long fromUid) {
		this.fromUid = fromUid;
	}

	
}
