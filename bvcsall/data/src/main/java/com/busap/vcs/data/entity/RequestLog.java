package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//接口日志
@Entity
@Table(name = "request_log")
public class RequestLog extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6125606064003503748L;

	private String referer;
	
	private String method;
	
	private String url;
	
	private String params;
	
	private String localIp;
	
	private String uid;
	
	private String reqTimestamp;
	
	private String reqSignature;
	
	private String headers;

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getReqTimestamp() {
		return reqTimestamp;
	}

	public void setReqTimestamp(String reqTimestamp) {
		this.reqTimestamp = reqTimestamp;
	}

	public String getReqSignature() {
		return reqSignature;
	}

	public void setReqSignature(String reqSignature) {
		this.reqSignature = reqSignature;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}
	
	
}
