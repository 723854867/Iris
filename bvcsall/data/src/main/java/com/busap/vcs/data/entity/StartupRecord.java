package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "startup_record")
public class StartupRecord extends BaseEntity{  
	 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6365825928742621126L;


	private String  ifa; 
	
	
	private String  vNo;
	


	public String getIfa() {
		return ifa;
	}

	public void setIfa(String ifa) {
		this.ifa = ifa;
	}

	public String getvNo() {
		return vNo;
	}

	public void setvNo(String vNo) {
		this.vNo = vNo;
	}


	
	
}
