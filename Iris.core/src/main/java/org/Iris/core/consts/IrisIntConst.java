package org.Iris.core.consts;

public class IrisIntConst extends IrisConstImpl<Integer> {

	public IrisIntConst(String key, int constId) {
		super(key, null,constId);
	}

	public IrisIntConst(String key, int value, int constId) {
		super(key, value, constId);
	}
	
	@Override
	protected Integer parseValue(String value) {
		return Integer.parseInt(value);
	}
}
