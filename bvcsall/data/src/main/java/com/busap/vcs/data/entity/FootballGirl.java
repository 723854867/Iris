package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//校园招募
@Entity
@Table(name = "football_girl")
public class FootballGirl extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8905527464360772121L;

	private String job;
	
	private String area;
	
	private String phone;

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
