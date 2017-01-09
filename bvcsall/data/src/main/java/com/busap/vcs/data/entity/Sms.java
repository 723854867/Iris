package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sms")
public class Sms extends BaseEntity {

	private static final long serialVersionUID = -6779472947860259775L;

	private String name;

	private String phoneNo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

}
