package org.Iris.core.consts;

public interface IrisConst<T> {
	
	int constId();

	String key();
	
	T parse(String value);
	
	T defaultValue();
}
