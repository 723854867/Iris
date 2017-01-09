package com.busap.vcs.data.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "advimage")
// 广告图片
public class AdvImage extends BaseEntity {
	private String title;// 广告标题
	private Date startTime;// 生效时间
	private Date endTime;// 失效时间
	@Transient
	private String start_time;		//生效时间,不入库
	@Transient
	private String end_time;		//生效时间,不入库
	private int status;// 状态
	private String imgBasePath;// 图片基础路径
	private String redirectUrl;//跳转地址
	private String operation;		//点击消息操作，user 打开个人中心、video打开视频详情页、activity打开活动详情页、web跳转到指定url
	private Long targetid;			//操作目标对象id，视频id或者活动id

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		if(startTime != null){
			this.start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startTime);			
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		if(endTime != null){
			this.end_time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endTime);			
		}
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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
		if(StringUtils.isNotBlank(start_time)){
			try {
				this.startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(start_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
		if(StringUtils.isNotBlank(end_time)){
			try {
				this.endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(end_time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
