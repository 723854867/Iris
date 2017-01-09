package com.busap.vcs.data.entity;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "operation_log")
public class OperationLog extends BaseEntity {
	
	private static final long serialVersionUID = -5215474115523571712L;
	private String moduleType;//操作模块
	private String reqType;//http请求类型
	private String operType;//操作类型
	private String reqUrl;//请求url
	private String referer;//
	private String objectid;//操作对象的id
	private String objectname;//操作对象的表明
	private Long uid;//操作人的姓名
	private String ip;
	
	private String uname;//操作人的姓名
	private String description;//操作描述
	private Long permissionId;

	public OperationLog() {
	}
	public OperationLog(String moduleType, String operType, String objectid,
			String objectname, Long uid, String uname, String description) {
		super();
		this.moduleType = moduleType;
		this.operType = operType;
		this.objectid = objectid;
		this.objectname = objectname;
		this.uid = uid;
		this.uname = uname;
		this.description = description;
	}
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getReqUrl() {
		return reqUrl;
	}
	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getObjectname() {
		return objectname;
	}
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getObjectid() {
		return objectid;
	}
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
}
