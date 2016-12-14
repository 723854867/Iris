package org.Iris.aliyun.bean;

public enum Region {

	CN_HANGZHOU("cn-hangzhou");
	
	private String mark;
	private Region(String mark) {
		this.mark = mark;
	}
	
	public String mark() {
		return mark;
	}
}
