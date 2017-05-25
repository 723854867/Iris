package org.Iris.app.jilu.storage.domain;

import java.util.List;

/**
 * 用户表
 * @author liusiyuan
 * 2017年5月3日
 */
public class SysUser {
	private int adminId;
	private String loginNo;
	private String userName;
	private String password;
	private int superFlag;
	private int lastLoginTime;
	private String lastLoginIp;
	private List<SysRole> sysRoleSet;
	public SysUser() {
		super();
	}

	public SysUser(int adminId, String loginNo, String userName, String password, int lastLoginTime, String lastLoginIp) {
		super();
		this.adminId = adminId;
		this.loginNo = loginNo;
		this.userName = userName;
		this.password = password;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
	}



	public SysUser(String loginNo, String userName, String password) {
		super();
		this.loginNo = loginNo;
		this.userName = userName;
		this.password = password;
	}

	public int getSuperFlag() {
		return superFlag;
	}

	public void setSuperFlag(int superFlag) {
		this.superFlag = superFlag;
	}

	public List<SysRole> getSysRoleSet() {
		return sysRoleSet;
	}

	public void setSysRoleSet(List<SysRole> sysRoleSet) {
		this.sysRoleSet = sysRoleSet;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public String getLoginNo() {
		return loginNo;
	}

	public void setLoginNo(String loginNo) {
		this.loginNo = loginNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(int lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
}
