package org.Iris.core.consts;

import org.Iris.core.exception.IllegalConstException;

abstract class IrisConstImpl<T> implements IrisConst<T>  {

	private int constId;
	private String key;
	private T defaultValue;
	
	public IrisConstImpl(String key, T defaultValue) {
		this(key, defaultValue, 0);
	}
	
	public IrisConstImpl(String key, T defaultValue, int constId) {
		this.key = key;
		this.constId = constId;
		this.defaultValue = defaultValue;
	}
	
	@Override
	public T parse(String value) {
		try {
			return parseValue(value);
		} catch (Exception e) {
			throw IllegalConstException.errorException(this, e);
		}
	}
	
	protected abstract T parseValue(String value);
	
	@Override
	public int constId() {
		return constId;
	}

	@Override
	public String key() {
		return this.key;
	}
	
	@Override
	public T defaultValue() {
		return defaultValue;
	}
}
