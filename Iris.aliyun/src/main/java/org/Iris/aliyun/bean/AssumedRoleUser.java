package org.Iris.aliyun.bean;

public class AssumedRoleUser {

	private String Arn;
	private String AssumedRoleId;
	
	public String getArn() {
		return Arn;
	}
	
	public void setArn(String arn) {
		Arn = arn;
	}
	
	public String getAssumedRoleId() {
		return AssumedRoleId;
	}
	
	public void setAssumedRoleId(String assumedRoleId) {
		AssumedRoleId = assumedRoleId;
	}
}
