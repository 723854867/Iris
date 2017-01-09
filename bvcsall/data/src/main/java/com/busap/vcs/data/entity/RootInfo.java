package com.busap.vcs.data.entity;

import java.io.Serializable;

import javax.persistence.metamodel.Attribute;


public class RootInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8847236486419431121L;


	private String name;
	
	
	private Class classType;
	
	private Attribute attribute;


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


	public Attribute getAttribute() {
		return attribute;
	}


	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	

}
