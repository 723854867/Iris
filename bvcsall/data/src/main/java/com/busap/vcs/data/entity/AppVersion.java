package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//直播房间
@Entity
@Table(name = "app_version")
public class AppVersion extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6227320241792494137L;
	
	
	private String appVersion; //版本信息

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	
}
