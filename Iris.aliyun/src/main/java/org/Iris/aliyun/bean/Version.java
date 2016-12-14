package org.Iris.aliyun.bean;

public enum Version {

	V_2015_04_01("2015-04-01");
	
	private String mark;
	private Version(String mark) {
		this.mark = mark;
	}
	public String mark() {
		return mark;
	}
}
