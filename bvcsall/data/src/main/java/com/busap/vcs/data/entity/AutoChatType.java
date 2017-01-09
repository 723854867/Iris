package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="auto_chat_type")
public class AutoChatType extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3052030387195150219L;

	private String name;
	
	private Integer status = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
