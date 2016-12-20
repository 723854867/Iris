package org.Iris.app.jilu.web;

import org.Iris.core.consts.IrisBoolConst;
import org.Iris.core.consts.IrisIntConst;
import org.Iris.core.consts.IrisLongConst;
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
	final IrisLongConst CUSTOMERID							= new IrisLongConst("customerId",0, 112);
	final IrisStrConst GOODSLIST							= new IrisStrConst("goodsList", 113);
	final IrisStrConst ORDERID								= new IrisStrConst("orderId", 114);
	final IrisStrConst ID_NUMBER							= new IrisStrConst("IDNumber", 115);
	final IrisStrConst ID_FRONTAGE							= new IrisStrConst("IDFrontage", 116);
	final IrisStrConst ID_BEHIND							= new IrisStrConst("IDBehind", 117);
	final IrisStrConst MEMO									= new IrisStrConst("memo", 118);
	final IrisStrConst GOODS_CODE							= new IrisStrConst("goodsCode", 119);
	final IrisStrConst ZH_NAME							    = new IrisStrConst("zhName", 120);
	final IrisStrConst US_NAME							    = new IrisStrConst("usName", 121);
	final IrisStrConst GOODS_FORMAT							= new IrisStrConst("goodsFormat", 122);
	final IrisStrConst CLASSIFICATION						= new IrisStrConst("classification", 123);
	final IrisStrConst ZH_BRAND								= new IrisStrConst("zhBrand", 124);
	final IrisStrConst US_BRAND								= new IrisStrConst("usBrand", 125);
	final IrisStrConst UNIT								    = new IrisStrConst("unit", 126);
	final IrisStrConst WEIGHT							    = new IrisStrConst("weight", 127);
	final IrisStrConst ALIAS							    = new IrisStrConst("alias", 128);
	final IrisStrConst BARCODE							    = new IrisStrConst("barcode", 129);
	final IrisStrConst SKU							        = new IrisStrConst("sku", 130);
	final IrisStrConst ADDGOODSLIST							= new IrisStrConst("addGoodsList",null, 131);
	final IrisStrConst UPDATEGOODSLIST					    = new IrisStrConst("updateGoodsList",null, 132);
	final IrisStrConst DELETEGOODSLIST						= new IrisStrConst("deleteGoodsList",null,133);
	final IrisIntConst PAGE									= new IrisIntConst("page", 134);
	final IrisIntConst PAGE_SIZE							= new IrisIntConst("pageSize", 135);
	
}
