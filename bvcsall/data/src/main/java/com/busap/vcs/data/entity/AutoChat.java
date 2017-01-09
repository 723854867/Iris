package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "auto_chat")
public class AutoChat extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3221016677048882046L;
	
	private String words;		//自动发言内容
	
	private Long uid;			//绑定到用户Id
	
	private Long typeId = 0l;	//类型ID
	
	private Integer status;		//状态 0 默认上线 1下线

	@Transient
	private String userName;
	
	@Transient
	private String typeName;
	
	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
}
