package org.Iris.core.util;

import java.util.Properties;

import org.Iris.core.consts.IrisConst;
import org.Iris.core.exception.IllegalConstException;

public class IrisProperties extends Properties {

	private static final long serialVersionUID = -1635951861537927001L;
	
	/**
	 * 获取 {@link IrisConst#key()} 所对应的属性，如果不存在或者转换错误则抛出异常
	 * 
	 * @param constant
	 * @return
	 * @throws IllegalConstException
	 */
	public <T> T get(IrisConst<T> constant) throws IllegalConstException {
		String val = getProperty(constant.key());
		if (null == val) 
			throw IllegalConstException.nullException(constant);
		return constant.parse(val);
	}
	
	/**
	 * 获取 {@link IrisConst#key()} 所对应的属性，如果不存在或者转换错误返回 {@link IrisConst#defaultValue()}
	 * 
	 * @param constant
	 * @return
	 */
	public <T> T getOptional(IrisConst<T> constant) {
		try {
			return get(constant);
		} catch (IllegalConstException e) {
			return constant.defaultValue();
		}
	}
}
