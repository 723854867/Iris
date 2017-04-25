package org.Iris.app.jilu.web;

import org.Iris.core.consts.IrisFloatConst;
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
	final IrisIntConst TYPE									= new IrisIntConst("type", 0, 102);
	final IrisStrConst ACCOUNT								= new IrisStrConst("account", 103);
	
	// 手机号码需要验证
	final IrisStrConst MOBILE								= new IrisStrConst("mobile", 104) {
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
	final IrisStrConst EMAIL								= new IrisStrConst("email", 105) {
		protected String parseValue(String value) {
			if (!Patterns.isEmail(value))
				throw IllegalConstException.errorException(this);
			return super.parseValue(value);
		};
	};
	
	final IrisStrConst CAPTCHA								= new IrisStrConst("captcha", 106);
	final IrisStrConst AVATAR								= new IrisStrConst("avatar", 107);
	final IrisStrConst ADDRESS								= new IrisStrConst("address", 108);
	final IrisStrConst TOKEN								= new IrisStrConst("token", 109);
	final IrisStrConst NAME									= new IrisStrConst("name", 110);
	final IrisLongConst CUSTOMER_ID							= new IrisLongConst("customerId",0, 111);
	final IrisStrConst GOODSLIST							= new IrisStrConst("goodsList", 112);
	final IrisStrConst ORDERID								= new IrisStrConst("orderId", 113);
	final IrisStrConst ID_NUMBER							= new IrisStrConst("IDNumber", 114);
	final IrisStrConst ID_FRONTAGE							= new IrisStrConst("IDFrontage", 115);
	final IrisStrConst ID_BEHIND							= new IrisStrConst("IDBehind", 116);
	final IrisStrConst MEMO									= new IrisStrConst("memo", 117);
	final IrisStrConst GOODS_CODE							= new IrisStrConst("goodsCode", 118);
	final IrisStrConst ZH_NAME							    = new IrisStrConst("zhName", 119);
	final IrisStrConst US_NAME							    = new IrisStrConst("usName", 120);
	final IrisStrConst GOODS_FORMAT							= new IrisStrConst("goodsFormat", 121);
	final IrisStrConst CLASSIFICATION						= new IrisStrConst("classification", 122);
	final IrisStrConst ZH_BRAND								= new IrisStrConst("zhBrand", 123);
	final IrisStrConst US_BRAND								= new IrisStrConst("usBrand", 124);
	final IrisStrConst UNIT								    = new IrisStrConst("unit", 125);
	final IrisLongConst WEIGHT							    = new IrisLongConst("weight",0, 126);
	final IrisStrConst ALIAS							    = new IrisStrConst("alias", 127);
	final IrisStrConst BARCODE							    = new IrisStrConst("barcode", 128);
	final IrisStrConst SKU							        = new IrisStrConst("sku", 129);
	final IrisStrConst ADDGOODSLIST							= new IrisStrConst("addGoodsList", null, 130);
	final IrisStrConst UPDATEGOODSLIST					    = new IrisStrConst("updateGoodsList", null, 131);
	final IrisStrConst DELETEGOODSLIST						= new IrisStrConst("deleteGoodsList", null, 132);
	final IrisIntConst PAGE									= new IrisIntConst("page", 1, 133);
	final IrisIntConst PAGE_SIZE							= new IrisIntConst("pageSize", 10, 134);
	final IrisLongConst MERCHANTID							= new IrisLongConst("merchantId", 135);
	final IrisLongConst TARGET_ID							= new IrisLongConst("targetId", 136);
	final IrisStrConst PACKETGOODSLIST						= new IrisStrConst("packetGoodsList", 137);
	final IrisStrConst PACKETID								= new IrisStrConst("packetId", 138);
	final IrisStrConst POSTAGE								= new IrisStrConst("postage", 139);
	final IrisStrConst EXPRESS								= new IrisStrConst("express", 140);
	final IrisStrConst EXPRESSCODE							= new IrisStrConst("expressCode", 141);
	final IrisStrConst LABEL								= new IrisStrConst("label", 142);
	final IrisStrConst LATITUDE								= new IrisStrConst("latitude", 143);
	final IrisStrConst LONGITUDE							= new IrisStrConst("longitude", 144);
	final IrisLongConst UNITPRICE							= new IrisLongConst("unitPrice",1, 126);
	final IrisStrConst QUERYID								= new IrisStrConst("queryId", 146);
	final IrisIntConst MOD									= new IrisIntConst("mod", 0, 147);
	final IrisStrConst VALUE								= new IrisStrConst("value", 148);
	final IrisLongConst GOODS_ID							= new IrisLongConst("goodsId", 149);
	final IrisStrConst CID								    = new IrisStrConst("cid", 150);
	final IrisStrConst CODE								    = new IrisStrConst("code", 151);
	final IrisStrConst OPENID								= new IrisStrConst("openId", 152);
	final IrisLongConst COUNT							    = new IrisLongConst("count",0, 153);
	final IrisStrConst KEY							        = new IrisStrConst("key", 154);
	final IrisFloatConst PRICE							    = new IrisFloatConst("price", 155);
	final IrisStrConst PHONES 							    = new IrisStrConst("phones", 156);
	final IrisIntConst START 								= new IrisIntConst("start", 157);
	final IrisIntConst END 									= new IrisIntConst("end", 158);
	final IrisStrConst OPERATION 							= new IrisStrConst("operation", 159);
	final IrisIntConst STOCKTIME 							= new IrisIntConst("stockTime", 160);
	final IrisStrConst INFO 								= new IrisStrConst("info", 161);
	final IrisIntConst TIME 								= new IrisIntConst("time", 162);
	final IrisStrConst FROMPACKETID							= new IrisStrConst("fromPacketId", 163);
	final IrisStrConst TOPACKETID							= new IrisStrConst("toPacketId", 164);
	final IrisLongConst ID									= new IrisLongConst("id", 165);
	final IrisStrConst IDS									= new IrisStrConst("ids", 166);
	final IrisStrConst BODY									= new IrisStrConst("body", 167);
	final IrisStrConst SUBJECT								= new IrisStrConst("subject", 168);
	final IrisStrConst OUTTRADENO							= new IrisStrConst("outtradeno", 169);
	final IrisFloatConst TOTALAMOUNT						= new IrisFloatConst("totalAmount", 170);
	final IrisStrConst RECEIPT								= new IrisStrConst("receipt", 171);
	final IrisStrConst PASSWORD								= new IrisStrConst("password", 172);
	final IrisStrConst OLDPWD								= new IrisStrConst("oldPwd", 173);
	final IrisStrConst NEWPWD								= new IrisStrConst("newPwd", 174);
	final IrisStrConst PAGEPATH								= new IrisStrConst("pagePath", 175);
	final IrisStrConst VERSION_NUM							= new IrisStrConst("versionNum", 176);
	final IrisIntConst STATUS								= new IrisIntConst("status", 177);
	final IrisStrConst LABELNUM								= new IrisStrConst("labelNum", 178);
}
