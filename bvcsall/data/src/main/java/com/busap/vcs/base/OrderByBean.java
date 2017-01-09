package com.busap.vcs.base;

public class OrderByBean {

	
	public OrderByBean(String sortProperty, int sortMode) {
		super();
		this.sortProperty = sortProperty;
		this.sortMode = sortMode;
	}
	
	public OrderByBean(String sortProperty, int sortMode,String sortClassAlias) {
		super();
		this.sortClassAlias = sortClassAlias;
		this.sortProperty = sortProperty;
		this.sortMode = sortMode;
	}

	public OrderByBean() {
		super();		
	}

	//升序
	public static int  SORT_MODE_ASC  = 0;
	
	//降序
	public static int  SORT_MODE_DESC = 1;
	
	//排序类别名
	private String sortClassAlias;
	
	//排序字段
	private String sortProperty;
	//排序方式
	private int sortMode = 1; 
	
	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}	
	
	public int getSortMode() {
		return sortMode;
	}

	public void setSortMode(int sortMode) {
		this.sortMode = sortMode;
	}

	public String getSortClassAlias() {
		return sortClassAlias;
	}

	public void setSortClassAlias(String sortClassAlias) {
		this.sortClassAlias = sortClassAlias;
	}
	
	
	
	
}
