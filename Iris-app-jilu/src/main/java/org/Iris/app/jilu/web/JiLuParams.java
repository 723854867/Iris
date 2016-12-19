package org.Iris.app.jilu.web;

import org.Iris.core.consts.IrisBoolConst;
import org.Iris.core.consts.IrisIntConst;
import org.Iris.core.consts.IrisStrConst;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.Patterns;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public interface JiLuParams {

	final IrisStrConst ACTION								= new IrisStrConst("action", 101);
	final IrisBoolConst SERIAL								= new IrisBoolConst("serial", false, 102);
	final IrisIntConst TYPE									= new IrisIntConst("type", 0, 103);
	final IrisStrConst ACCOUNT								= new IrisStrConst("account", 104);
	
	// 手机号码需要验证
	final IrisStrConst MOBILE								= new IrisStrConst("mobile", 105) {
		protected String parseValue(String value) {
			PhoneNumberUtil util = PhoneNumberUtil.getInstance();
			try {
				PhoneNumber number = util.parse(value, null);
				if (!util.isValidNumber(number))
					throw IllegalConstException.errorException(this);
				return "+" + number.getCountryCode() + number.getNationalNumber();
			} catch (NumberParseException e) {
				throw IllegalConstException.errorException(this);
			}
		};
	};
	
	// 邮箱号码需要验证
	final IrisStrConst EMAIL								= new IrisStrConst("email", 106) {
		protected String parseValue(String value) {
			if (!Patterns.isEmail(value))
				throw IllegalConstException.errorException(this);
			return super.parseValue(value);
		};
	};
	
	final IrisStrConst CAPTCHA								= new IrisStrConst("captcha", 107);
	final IrisStrConst AVATAR								= new IrisStrConst("avatar", 108);
	final IrisStrConst ADDRESS								= new IrisStrConst("address", 109);
	final IrisStrConst TOKEN								= new IrisStrConst("token", 110);
	final IrisStrConst NAME									= new IrisStrConst("name", 111);
	final IrisStrConst RECEIVEId							= new IrisStrConst("receiveId", 112);
	final IrisStrConst GOODSLIST							= new IrisStrConst("goodsList", 113);
	final IrisStrConst ORDERID								= new IrisStrConst("orderId", 114);
	final IrisStrConst ID_NUMBER							= new IrisStrConst("IDNumber", 115);
	final IrisStrConst ID_FRONTAGE							= new IrisStrConst("IDFrontage", 116);
	final IrisStrConst ID_BEHIND							= new IrisStrConst("IDBehind", 117);
	final IrisStrConst MEMO									= new IrisStrConst("memo", 118);
	final IrisIntConst PAGE									= new IrisIntConst("page", 119);
	final IrisIntConst PAGE_SIZE							= new IrisIntConst("pageSize", 120);
	
}
