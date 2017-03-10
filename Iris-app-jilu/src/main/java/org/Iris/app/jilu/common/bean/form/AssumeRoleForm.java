package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.AppConfig;

import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse.Credentials;

public class AssumeRoleForm {

	private String bucket;
	private String endpoint;
	private String expiration;
	private long expireTime;
	private String accessKeyId;
	private String securityToken;
	private String accessKeySecret;
	
	public AssumeRoleForm() {}
	
	public AssumeRoleForm(AssumeRoleResponse response) {
		this.bucket = AppConfig.getAliyunOssBucket();
		this.endpoint = AppConfig.getAliyunOssEndpoint();
		Credentials credentials = response.getCredentials();
		this.expiration = credentials.getExpiration();
		this.accessKeyId = credentials.getAccessKeyId();
		this.securityToken = credentials.getSecurityToken();
		this.accessKeySecret = credentials.getAccessKeySecret();
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getExpiration() {
		return expiration;
	}
	
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
