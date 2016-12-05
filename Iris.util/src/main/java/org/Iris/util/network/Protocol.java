package org.Iris.util.network;

public enum Protocol {

	SMTP("smtp");
	
	private String mark;
	private Protocol(String mark) {
		this.mark = mark;
	}
	public String mark() {
		return mark;
	}
}
