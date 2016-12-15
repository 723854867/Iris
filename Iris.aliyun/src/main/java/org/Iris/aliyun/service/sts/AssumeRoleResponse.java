package org.Iris.aliyun.service.sts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.Iris.aliyun.bean.AssumedRoleUser;
import org.Iris.aliyun.bean.Credentials;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AssumeRoleResponse")
public class AssumeRoleResponse {

	private String RequestId;
	private Credentials Credentials;
	private AssumedRoleUser AssumedRoleUser;
	
	public String getRequestId() {
		return RequestId;
	}
	
	public void setRequestId(String requestId) {
		RequestId = requestId;
	}
	
	public Credentials getCredentials() {
		return Credentials;
	}
	
	public void setCredentials(Credentials credentials) {
		Credentials = credentials;
	}
	
	public AssumedRoleUser getAssumedRoleUser() {
		return AssumedRoleUser;
	}
	
	public void setAssumedRoleUser(AssumedRoleUser assumedRoleUser) {
		AssumedRoleUser = assumedRoleUser;
	}
}
