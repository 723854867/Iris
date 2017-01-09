package com.busap.vcs.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="invite_info")//邀请好友信息
public class InviteInfo extends BaseEntity {

	private static final long serialVersionUID = -1608944135763923906L;
	 @Column(name = "platform_name")
	private String platformName;//邀请的平台名称
	 @Column(name = "platform_mark")
	private String platformMark;//邀请的平台标识
	 @Column(name = "title")
	private String title;//标题
	 @Column(name = "pic_path")
	private String picPath;//邀请图片路径
	 @Column(name = "description")
	private String description;//邀请描述信息
	 @Column(name = "status")
	private Integer status ;//是否可用 0不可用 1可用
	 @Column(name = "modify_name")
	private String modifyName;//修改人名称
	 @Column(name = "invite_info_url")
	private String inviteInfoUrl;//邀请信息url
	 
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformMark() {
		return platformMark;
	}
	public void setPlatformMark(String platformMark) {
		this.platformMark = platformMark;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	public String getInviteInfoUrl() {
		return inviteInfoUrl;
	}
	public void setInviteInfoUrl(String inviteInfoUrl) {
		this.inviteInfoUrl = inviteInfoUrl;
	}
	
	
}
