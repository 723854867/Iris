package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

public class SysPermission {
	private int permissionId;
	private int pid;
	private String permissionName;
	private String href;
	private String permission;
	private int type;
	private int created;
	private int updated;
	private int delFlag;

	public SysPermission() {
		super();
	}

	public SysPermission(int pid, String permissionName, String href, int type) {
		super();
		int time = DateUtils.currentTime();
		this.pid = pid;
		this.permissionName = permissionName;
		this.href = href;
		this.type = type;
		this.created = time; 
		this.updated = time;
	}

	public int getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(int permissionId) {
		this.permissionId = permissionId;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(int delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "SysPermission [permissionId=" + permissionId + ", pid=" + pid + ", permissionName=" + permissionName
				+ ", href=" + href + ", permission=" + permission + ", type=" + type + ", created=" + created
				+ ", updated=" + updated + ", delFlag=" + delFlag + "]";
	}
	
}
