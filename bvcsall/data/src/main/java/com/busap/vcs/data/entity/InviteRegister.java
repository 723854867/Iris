package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "invite_register")
public class InviteRegister extends BaseEntity {

	private static final long serialVersionUID = 6190392240325748001L;
	
	private String inviteUid;

	public String getInviteUid() {
		return inviteUid;
	}

	public void setInviteUid(String inviteUid) {
		this.inviteUid = inviteUid;
	}
	
	
}
