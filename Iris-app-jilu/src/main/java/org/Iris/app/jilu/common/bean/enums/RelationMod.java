package org.Iris.app.jilu.common.bean.enums;

public enum RelationMod {

	FRIEDN(1);
	
	private int mark;
	
	private RelationMod(int mark) {
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final boolean isMod(int modVal, RelationMod mod) {
		return (mod.mark & modVal) == modVal;
	}
}
