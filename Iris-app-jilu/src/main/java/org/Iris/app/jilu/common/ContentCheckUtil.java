package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.Patterns;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public enum ContentCheckUtil {

	INSTANCE;
	
	public String checkAccount(AccountType type, String account) {
		switch (type) {
		case EMAIL:
			if(!Patterns.isEmail(account))
				return null;
			return account;
		default:
			return checkMobile(account);
		}
	}

	public String checkMobile(String mobile) {
		PhoneNumberUtil util = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber number = util.parse(mobile, null);
			if (!util.isValidNumber(number))
				throw IllegalConstException.errorException(JiLuParams.MOBILE);
			return "+" + number.getCountryCode() + number.getNationalNumber();
		} catch (NumberParseException e) {
			throw IllegalConstException.errorException(JiLuParams.MOBILE);
		}
	}
}
