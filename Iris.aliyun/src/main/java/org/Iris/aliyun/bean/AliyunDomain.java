package org.Iris.aliyun.bean;

public enum AliyunDomain {

	STS("sts.aliyuncs.com");
	
	private String mark;
	private AliyunDomain(String mark) {
		this.mark = mark;
	}
	public String mark() {
		return mark;
	}
}
