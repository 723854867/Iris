package org.Iris.app.jilu.storage.domain;

import java.util.List;

import org.Iris.util.lang.DateUtils;

/**
 * 角色表
 * @author liusiyuan
 * 2017年5月3日
 */
public class SysRole {
	private int roleId;
	private String roleName;
	private int created;
	private int updated;
	private List<SysPermission> sysPermissions;
	public SysRole() {
		super();
	}
	public SysRole(String roleName) {
		super();
		int time = DateUtils.currentTime();
		this.roleName = roleName;
		this.created = time;
		this.updated = time;
	}
	public List<SysPermission> getSysPermissions() {
		return sysPermissions;
	}
	public void setSysPermissions(List<SysPermission> sysPermissions) {
		this.sysPermissions = sysPermissions;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}
}
