package org.Iris.core.consts;

import org.Iris.util.lang.StringUtil;

public class IrisStrConst extends IrisConstImpl<String> {

	public IrisStrConst(String key, String value) {
		super(key, value);
	}

	public IrisStrConst(String key, String value, int constId) {
		super(key, value, constId);
	}
	
	public IrisStrConst(String key, int constId) {
		super(key, null, constId);
	}
	
	@Override
	protected String parseValue(String value) {
		return StringUtil.trimWhitespace(value);
	}
}
