package org.Iris.aliyun.service.sts;

import org.Iris.aliyun.AliyunParam;
import org.Iris.aliyun.bean.Action;
import org.Iris.aliyun.bean.AliyunDomain;
import org.Iris.aliyun.service.AliyunRequest;

public class AssumeRoleRequest extends AliyunRequest {

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
	
	public AssumeRoleRequest setDurationSeconds(String durationSeconds) { 
		return (AssumeRoleRequest) addAliyunParam(AliyunParam.DurationSeconds, durationSeconds);
	}
}
