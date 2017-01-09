package com.busap.vcs.data.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.busap.vcs.data.enums.Platform;

//系统消息通知
@Entity
@Table(name = "system_mess")
public class SystemMess extends BaseEntity{  
	 
	private static final long serialVersionUID = -8258234756212522540L;
	
	private String  title;    		//标题
	
	private String  destUser = "all";		//目标用户 
	
	private String  content;		//通知内容
	
	private String operation;		//点击消息操作，app 打开应用首页、video打开视频详情页、activity打开活动详情页
	
	private Long targetid;			//操作目标对象id，视频id或者活动id

	@Column(name = "target_url",columnDefinition = "varchar(255) NULL",nullable=true)
	private String targetUrl; //操作目标对象url
	
	private String platform = Platform.ALL.getName();  	//推送平台，android ios
	
	private String isplan = "0";						//是否计划发布，默认0 立即发布，1为计划发布
	
	private Integer liveTime;		//消息发出后的有效时间，单位：分钟
	
	@Transient
	private String pubTime;								//计划发布时间,不入库
	
	private Date publishTime;				//计划发布时间

	// 视频图片
	private String imagePath;

	private String stat = "0";

	public String getTitle() {
		return title;
	}

	public String getDestUser() {
		return destUser;
	}

	public void setDestUser(String destUser) {
		this.destUser = destUser;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getIsplan() {
		return isplan;
	}

	public void setIsplan(String isplan) {
		this.isplan = isplan;
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

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public Long getTargetid() {
		return targetid;
	}

	public void setTargetid(Long targetid) {
		this.targetid = targetid;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
		if(StringUtils.isNotBlank(pubTime)){
			try {
				this.publishTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(pubTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Integer getLiveTime() {
		return liveTime;
	}

	public void setLiveTime(Integer liveTime) {
		this.liveTime = liveTime;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
