package org.Iris.aliyun.service;

import org.Iris.aliyun.policy.Policy;
import org.Iris.util.common.SerializeUtil;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;

public class StsService {

	private String region;
	private String version;
	private String accessKeyId;
	private String accessKeySecret;

	private DefaultAcsClient client;

	public void init() {
		IClientProfile profile = DefaultProfile.getProfile(region, accessKeyId, accessKeySecret);
		this.client = new DefaultAcsClient(profile);
	}
	
	public AssumeRoleResponse assumeRole(String roleArn, String roleSessionName) {
		AssumeRoleRequest request = _request(roleArn, roleSessionName);
		try {
			return client.getAcsResponse(request);
		} catch (ClientException e) {
			throw new RuntimeException("Aliyun sts - AssumeRole invoke failure!", e);
		}
	}
	
	public AssumeRoleResponse assumeRole(String roleArn, String roleSessionName, int durationSeconds) {
		AssumeRoleRequest request = _request(roleArn, roleSessionName);
		request.setDurationSeconds(Long.valueOf(durationSeconds));
		try {
			return client.getAcsResponse(request);
		} catch (ClientException e) {
			throw new RuntimeException("Aliyun sts - AssumeRole invoke failure!", e);
		}
	}

	public AssumeRoleResponse assumeRole(String roleArn, String roleSessionName, Policy policy) {
		AssumeRoleRequest request = _request(roleArn, roleSessionName);
		request.setPolicy(SerializeUtil.JsonUtil.GSON.toJson(policy));
		try {
			return client.getAcsResponse(request);
		} catch (ClientException e) {
			throw new RuntimeException("Aliyun sts - AssumeRole invoke failure!", e);
		}
	}
	
	public AssumeRoleResponse assumeRole(String roleArn, String roleSessionName, Policy policy, int durationSeconds) {
		AssumeRoleRequest request = _request(roleArn, roleSessionName);
		request.setPolicy(SerializeUtil.JsonUtil.GSON.toJson(policy));
		request.setDurationSeconds(Long.valueOf(durationSeconds));
		try {
			return client.getAcsResponse(request);
		} catch (ClientException e) {
			throw new RuntimeException("Aliyun sts - AssumeRole invoke failure!", e);
		}
	}
	
	private AssumeRoleRequest _request(String roleArn, String roleSessionName) {
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setVersion(version);
		request.setMethod(MethodType.POST);
		request.setProtocol(ProtocolType.HTTPS);
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		return request;
	}
	
	public void dispose() {
		this.client = null;
		this.region = null;
		this.version = null;
		this.accessKeyId = null;
		this.accessKeySecret = null;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
}
