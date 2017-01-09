package com.busap.vcs.base;

import javax.persistence.TemporalType;


public class ParameterBean{
	
	

	public ParameterBean(String name, Object value, TemporalType type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}
	
	private String name;
	
	private Object value;
	
	private TemporalType type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public TemporalType getType() {
		return type;
	}

	public void setType(TemporalType type) {
		this.type = type;
	}


}