package org.Iris.aliyun.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
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
