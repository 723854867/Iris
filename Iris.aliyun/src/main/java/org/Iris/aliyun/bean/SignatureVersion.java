package org.Iris.aliyun.bean;

public enum SignatureVersion {

	V_1_POINT_0("1.0");
	
	private String mark;
	private SignatureVersion(String mark) {
		this.mark = mark;
	}
	public String mark() {
		return mark;
	}
}
