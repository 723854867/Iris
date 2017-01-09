package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "promotion_record")
public class PromotionRecord extends BaseEntity{  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8562325625268869576L;
	
	private String  mac;       
	
	private String  ifa; 
	
	private String  callbackUrl;  
	
	private String  fromType;
	

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

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	
	
}
