package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "spread")
public class Spread extends BaseEntity {

	private static final long serialVersionUID = -2349980995915125925L;
	
	private String inviteCode;

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	

}
