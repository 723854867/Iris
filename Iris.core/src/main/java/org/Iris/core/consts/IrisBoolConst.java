package org.Iris.core.consts;

public class IrisBoolConst extends IrisConstImpl<Boolean> {

	public IrisBoolConst(String key, boolean value) {
		super(key, value);
	}

	public IrisBoolConst(String key, boolean value, int constId) {
		super(key, value, constId);
	}
	
	@Override
	protected Boolean parseValue(String value) {
		return Boolean.valueOf(value);
	}
}
