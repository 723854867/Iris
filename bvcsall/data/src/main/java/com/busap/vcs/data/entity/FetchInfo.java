package com.busap.vcs.data.entity;

import java.io.Serializable;


public class FetchInfo implements Serializable {
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 5305625490726797588L;


	private String name;
	
	
	private Class classType;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Class getClassType() {
		return classType;
	}


	public void setClassType(Class classType) {
		this.classType = classType;
	}
	
	

}
