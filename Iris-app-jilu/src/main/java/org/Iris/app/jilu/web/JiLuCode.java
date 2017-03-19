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
	CUSTOMER_NOT_EXIST(205, "customer is not exist"),
	ORDER_GOODS_IS_LOCK(206, "order_goods {0} is lock"),
	GOODS_NOT_EXIST(207, "goods {0} not exist"),
	ORDER_GOODS_IS_EXIST(208, "order_goods {0} is exist"),
	ORDER_GOODS_NOT_EXIST(209, "order_goods {0} not exist"),
	GOODSLIST_CAN_NOT_CHANGE(210, "goodsList can not change"),
	TARGET_MERCHANT_NOT_EXIST(211, "target merchant is not exist"),
	SELF_LIMIT(212, "self limit"),
	ORDER_GOODS_NOT_CHANGING(213, "order_goods {0} not changing"),
	RELATION_EXIST(214, "relation exist"),
	RELATION_NOT_EXIST(215, "relation not exist"),
	FRIEND_APPLY_NOT_EXIST(216, "friend apply not exist"),
	PACKET_NOT_EXIST(217, "packet {0} not exist"),
	GOODS_DELETE_LIMIT(218, "can not delete other merchant goods {0}"),
	ACCOUNT_ALREADY_BINDED(219, "account {0} already binded"),
	WYYX_ACCOUNT_CREATE_FAIL(220, "get wyyx account fail"),
	GET_WEIXIN_ACCESSTOKEN_FAIL(221, "get weixin access token fail"),
	WEIXIN_ACCESSTOKEN_EXPAIRED(222, "weixin access_token expired"),
	ACCESSTOKEN_ERROR(223, "weixin access_token error"),
	REFRESH_TOKEN_FAIL(224, "weixin refresh token fail"),
	UNIFORM_ORDER_FAIL(225, "weixin uniform order fail"),
	TRANSFORM_ORDER_CONNOT_UPDATED(226, "transform order connot updated"),
	ORDER_CONNOT_OPTION(227, "order connot option"),
	CHANGE_SUM_BIGGER(228, "change sum bigger"),
	PACKET_COUNT_ERROR(229, "order packet count error"),
	ORDER_MEMO_CANNOT_EDIT(230, "this order memo can't edit");
	
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
