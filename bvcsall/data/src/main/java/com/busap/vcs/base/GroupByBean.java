package com.busap.vcs.base;

public class GroupByBean {

	
	public GroupByBean(String groupClassAlias,String groupProperty) {
		super();
		this.groupClassAlias = groupClassAlias;
		this.groupProperty = groupProperty;
	}

	public GroupByBean() {
		super();		
	}

	
	//分组类别名
	private String groupClassAlias;
	
	//分组字段
	private String groupProperty;

	public String getGroupClassAlias() {
		return groupClassAlias;
	}

	public void setGroupClassAlias(String groupClassAlias) {
		this.groupClassAlias = groupClassAlias;
	}

	public String getGroupProperty() {
		return groupProperty;
	}

	public void setGroupProperty(String groupProperty) {
		this.groupProperty = groupProperty;
	}
	
	

	
	
	
	
	
}
