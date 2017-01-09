package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "loop_banner")
public class Banner extends BaseEntity {

	private static final long serialVersionUID = 7778400864870158928L;

	@Column(name = "title",columnDefinition = "varchar(20) NOT NULL ",nullable=false)
	private String title;		//标题
	@Column(name = "img_src",columnDefinition = "varchar(200) NOT NULL ",nullable=false)
	private String imgSrc;		//图片路径
	@Column(name = "banner_type",columnDefinition = "int NULL DEFAULT 0",nullable=true)
	private Integer bannerType;//显示1 直播 2视频
	@Column(name = "target_type",columnDefinition = "varchar(10) NULL",nullable=true)
	private String targetType;	//跳转类型 activity：活动、video：视频、user：用户中心、h5：页面 live:直播 rvideo:视频推荐 liveAct 直播活动
	@Column(name = "target_url",columnDefinition = "varchar(250) NULL",nullable=true)
	private String targetUrl;	//h5地址
	@Column(name = "target_id",columnDefinition = "bigint NULL",nullable=true)
	private Long targetId;		//跳转目标id
	@Column(name = "tag",columnDefinition = "varchar(10) NULL",nullable=true)
	private String tag;			//角标，hot 0/new 1/火爆 2/-1不显示角标
	@Column(name = "show_able",columnDefinition = "int NULL DEFAULT 0",nullable=true)
	private Integer showAble = 0;//显示0 默认、隐藏1
	@Column(name = "order_num",columnDefinition = "int NULL DEFAULT 0",nullable=true)
	private Integer orderNum = 0;//显示顺序，数字越大，越靠前
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Long getTargetId() {
		return targetId;
	}
	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Integer getShowAble() {
		return showAble;
	}
	public void setShowAble(Integer showAble) {
		this.showAble = showAble;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Integer getBannerType() {
		return bannerType;
	}

	public void setBannerType(Integer bannerType) {
		this.bannerType = bannerType;
	}
}
