package org.Iris.app.jilu.storage.domain;
/**
 * 角色权限表
 * @author liusiyuan
 * 2017年5月3日
 */
public class SysRolePermission {
	private int id;
	private int permissionId;
	private int roleId;
	public SysRolePermission() {
		super();
	}
	public SysRolePermission(int id, int permissionId, int roleId) {
		super();
		this.id = id;
		this.permissionId = permissionId;
		this.roleId = roleId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
