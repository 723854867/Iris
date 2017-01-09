package com.busap.vcs.data.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;


@Entity
@Table(name = "recommand_position")
// 推荐位设置
public class RecommandPosition extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6622808639824058830L;
	
	private Integer page;	//推荐位页码
	private Integer position;//位置
	private String title;// 推荐位标题
	private String description;//描述
	private String picPath;		//封面图地址
	private Date startTime;// 生效时间
	private Date endTime;// 失效时间
	@Transient
	private String start_time;		//生效时间,不入库
	@Transient
	private String end_time;		//生效时间,不入库
	private int status;// 状态 1 启用 默认，0 停用
	private String redirectUrl;//跳转地址
	private String operation;		//点击消息操作，user 打开个人中心、video打开视频详情页、activity打开活动详情页、label热门活动视频列表、web跳转到指定url
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

}
