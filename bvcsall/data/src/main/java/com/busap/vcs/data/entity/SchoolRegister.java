package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//校园招募
@Entity
@Table(name = "school_register")
public class SchoolRegister extends BaseEntity {

	private static final long serialVersionUID = -4410356562701843741L;
	
	private String name;
	
	private Integer age;
	
	private String school;
	
	private String wechat;
	
	private String qq;
	
	private String phone;
	
	private String introduction;
	
	private String img;
	
	private String inviteCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	
	

}
