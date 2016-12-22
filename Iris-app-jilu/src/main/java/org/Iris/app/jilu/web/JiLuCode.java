package org.Iris.app.jilu.web;

import org.Iris.core.service.locale.ICode;
import org.Iris.util.lang.StringUtil;

/**
 * 注意 101 ~ 200 已经被 {@link JiLuParams} 中的各种网络参数定义了
 * 
 * @author Ahab
 */
public enum JiLuCode implements ICode {
	
	CAPTCHA_GET_CD(201, "captcha get frequently"),
	CAPTCHA_COUNT_LIMIT(202, "captcha count limit"),
	CAPTCHA_ERROR(203, "captcha error"),
	ORDER_IS_LOCK(204, "order is lock"),
	CUSTOMER_NOT_EXIST(205, "customer is not exist");
	
	private int code;
	private String defaultVaule;
	
	private JiLuCode(int code, String defaultValue) {
		this.code = code;
		this.defaultVaule = defaultValue;
	}

	@Override
	public int constId() {
		return this.code;
	}

	@Override
	public String key() {
		return CODE_PREFIX + name();
	}

	@Override
	public String parse(String value) {
		this.defaultVaule = StringUtil.trimWhitespace(value);
		return defaultVaule;
	}

	@Override
	public String defaultValue() {
		return this.defaultVaule;
	}
}
