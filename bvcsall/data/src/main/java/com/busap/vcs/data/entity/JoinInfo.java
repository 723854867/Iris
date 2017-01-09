package com.busap.vcs.data.entity;

import java.io.Serializable;

import javax.persistence.criteria.JoinType;

public class JoinInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7319478411632017646L;

	private String name;
	
	private String rootName;
	
	private String joinName;
	
	private String collectionType;
	
	private JoinType joinType;
	
	private Class classType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	public String getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(String collectionType) {
		this.collectionType = collectionType;
	}

	public Class getClassType() {
		return classType;
	}

	public void setClassType(Class classType) {
		this.classType = classType;
	}

	public String getRootName() {
		return rootName;
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getJoinName() {
		return joinName;
	}

	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}
	
	
	
	
	

}
