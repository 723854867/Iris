package com.busap.vcs.data.enums;


/**
 * 用户状态
 * @author dmsong
 *
 */
public enum UserStatus {

	active(0,"激活"),
	gag(1,"禁言"),
	lock(2,"封号");
	
	private Integer stat;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	private UserStatus(Integer stat,String name){
		this.name=name;
		this.setStat(stat);
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

}
