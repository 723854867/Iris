package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "feedback")
public class Feedback extends BaseEntity {

	private static final long serialVersionUID = -5996559603308462399L;
	
	//反馈内容
	@Column(name = "content",columnDefinition = "varchar(500) NOT NULL ",nullable=false)
	private String content;		
	//联系方式
	@Column(name = "contact",columnDefinition = "varchar(100) NOT NULL ",nullable=false)
	private String contact;

	private Long status;
	
	private String appVersion;

	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	
	
}
