package org.Iris.aliyun.service.sts;

import org.Iris.aliyun.AliyunParam;
import org.Iris.aliyun.bean.Action;
import org.Iris.aliyun.bean.AliyunDomain;

public class AssumeRoleRequest extends AliyunStsRequest {

	public AssumeRoleRequest(String accessKeyId, String accessKeySecret) {
		super(Action.AssumeRole, AliyunDomain.STS, accessKeyId, accessKeySecret);
	}
	
	public AssumeRoleRequest setRoleArn(String roleArn) {
		return (AssumeRoleRequest) addAliyunParam(AliyunParam.RoleArn, roleArn);
	}
	
	public AssumeRoleRequest setRoleSessionName(String roleSessionName) {
		return (AssumeRoleRequest) addAliyunParam(AliyunParam.RoleSessionName, roleSessionName);
	}
	
	public AssumeRoleRequest setPolicy(String policy) { 
		return (AssumeRoleRequest) addAliyunParam(AliyunParam.Policy, policy);
	}
	
	public AssumeRoleRequest setDurationSeconds(int durationSeconds) { 
		return (AssumeRoleRequest) addAliyunParam(AliyunParam.DurationSeconds, String.valueOf(durationSeconds));
	}
}
