package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "activation_record")
public class ActivationRecord extends BaseEntity{  
	 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8074683675860716179L;

	private String  mac;       
	
	private String  ifa; 
	
	private String  fromType;
	
	private String  userId;
	
	private String  name;
	

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIfa() {
		return ifa;
	}

	public void setIfa(String ifa) {
		this.ifa = ifa;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
