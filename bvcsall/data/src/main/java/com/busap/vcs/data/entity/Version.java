package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.busap.vcs.constants.BicycleConstants;

/*
 * 版本表
 */
@Entity
@Table(name = "promote_version")
public class Version extends BaseEntity{
   
	private static final long serialVersionUID = -1616650765280537166L;

	private int newVersion; //最新版本
	
	private int forceVersion; //强制版本 
	
	private String  downloadUrl;//安装包下载地址
	
	private String type;//ios or android
	
	private int versionType=BicycleConstants.CLIENT_UPGRADE_TYPE_TIP;//0:忽略 1：提示 2：强制 
	
	private String  description;//描述  换行符<br>
	

	public int getVersionType() {
		return versionType;
	}

	public void setVersionType(int versionType) {
		this.versionType = versionType;
	}

	public int getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(int newVersion) {
		this.newVersion = newVersion;
	}

	public int getForceVersion() {
		return forceVersion;
	}

	public void setForceVersion(int forceVersion) {
		this.forceVersion = forceVersion;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
}
