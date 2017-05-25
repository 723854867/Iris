package org.Iris.app.jilu.storage.domain;
/**
 * 用户角色表
 * @author liusiyuan
 * 2017年5月3日
 */
public class SysUserRole {
	private int id;
	private long userId;
	private int roleId;
	public SysUserRole() {
		super();
	}
	public SysUserRole( long userId, int roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
