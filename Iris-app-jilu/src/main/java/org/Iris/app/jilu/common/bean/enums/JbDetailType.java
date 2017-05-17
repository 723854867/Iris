package org.Iris.app.jilu.common.bean.enums;

public enum JbDetailType {

	CZ(0),
	BUE_LABEL(1),
	ORDER(2);
	
	private int type;
	
	private JbDetailType(int type){
		this.type = type;
	}
	
	public int type(){
		return this.type;
	}
}
