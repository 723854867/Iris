package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//校园招募
@Entity
@Table(name = "bstar")
public class Bstar extends BaseEntity {


	private static final long serialVersionUID = -1951055577765589466L;

	private Integer sex;  //性别，1：男 ，0：女
	
	private String number;
	
	private String phone;

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	
}
