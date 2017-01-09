package com.busap.vcs.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 直播活动
 * @author Administrator
 *
 */
@Entity
@Table(name = "live_activity")
public class LiveActivity extends BaseEntity{  
	 
	private static final long serialVersionUID = 6076925457215898252L;
	private String  title;       //活动名称
	private String  cover;  //封面
	private String  description;   //描述 
	private String  giftIds; //专属礼物id，多个id之间用逗号间隔
	private String  historyGiftIds; //历史设置过的专属礼物id，多个id之间用逗号间隔
	private Integer status; //状态,1:上线，0：下线
	private Integer showCountOfTop;//排行榜显示个数
	private Date startTime;//开始时间
	private Date endTime;//结束时间
	private Integer type; //此活动类型，1：APP，2：H5 默认为app
	private Long startTimeMsec;  //开始时间毫秒
	private Long endTimeMsec;  //结束时间毫秒
	private Integer orderNum = 0;//权重
	@Transient
	private String selfPosition; //用户自己的排名
	@Transient
	private String selfGiftNumber; //用户自己送的专属礼物数量
	@Transient
	private Integer isJoin; //判断用户是否参与了此活动，1：是，0：否
	@Transient
	private Integer canJoin; //判断用户是否可以参与，1：是，0：否
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGiftIds() {
		return giftIds;
	}
	public void setGiftIds(String giftIds) {
		this.giftIds = giftIds;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getShowCountOfTop() {
		return showCountOfTop;
	}
	public void setShowCountOfTop(Integer showCountOfTop) {
		this.showCountOfTop = showCountOfTop;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Integer getIsJoin() {
		return isJoin;
	}
	public void setIsJoin(Integer isJoin) {
		this.isJoin = isJoin;
	}
	public String getSelfPosition() {
		return selfPosition;
	}
	public void setSelfPosition(String selfPosition) {
		this.selfPosition = selfPosition;
	}
	public String getSelfGiftNumber() {
		return selfGiftNumber;
	}
	public void setSelfGiftNumber(String selfGiftNumber) {
		this.selfGiftNumber = selfGiftNumber;
	}
	public Long getStartTimeMsec() {
		if(startTime!=null)
	        return startTime.getTime();
	    	else
	    		return null;
	}
	public void setStartTimeMsec(Long startTimeMsec) {
		this.startTimeMsec = startTimeMsec;
	}
	public Long getEndTimeMsec() {
		if(endTime!=null)
	        return endTime.getTime();
	    	else
	    		return null;
	}
	public void setEndTimeMsec(Long endTimeMsec) {
		this.endTimeMsec = endTimeMsec;
	}
	public Integer getCanJoin() {
		Date current = new Date();
		if (current.after(startTime) && current.before(endTime) && status ==1) {
			return 1;
		} else if(current.before(startTime)){
			return 2;
		}
		return 0;
	}
	public void setCanJoin(Integer canJoin) {
		this.canJoin = canJoin;
	}
	public String getHistoryGiftIds() {
		return historyGiftIds;
	}
	public void setHistoryGiftIds(String historyGiftIds) {
		this.historyGiftIds = historyGiftIds;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
