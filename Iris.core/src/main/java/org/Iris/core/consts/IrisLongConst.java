package org.Iris.core.consts;

public class IrisLongConst extends IrisConstImpl<Long> {

	public IrisLongConst(String key, long value) {
		super(key, value);
	}

	public IrisLongConst(String key, long value, int constId) {
		super(key, value, constId);
	}
	@Override
	protected Long parseValue(String value) {
		return Long.parseLong(value);
	}
}
