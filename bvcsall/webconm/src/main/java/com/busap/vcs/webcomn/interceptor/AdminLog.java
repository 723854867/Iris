package com.busap.vcs.webcomn.interceptor;

/**
 * Created by djyin on 9/9/2014.
 */
public class AdminLog {

	private String reqUrl;//url
	
	private String reqType;//请求方法类型
	
	private String referer;
	
	private String description;//存放请求参数
	
	private String objectid;//请求对象id
	
	public AdminLog(){}
	
	public AdminLog(String reqUrl, String reqType, String referer,
			String description, String objectid) {
		super();
		this.reqUrl = reqUrl;
		this.reqType = reqType;
		this.referer = referer;
		this.description = description;
		this.objectid = objectid;
	}

	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
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
}
