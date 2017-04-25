package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
/**
 * 商户寄件人信息添加
 * @author 樊水东
 * 2017年4月25日
 */
public class MERCHANT_SENDINFO_SAVE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String sendName = session.getKVParam(JiLuParams.NAME);
		String sendAddress = session.getKVParam(JiLuParams.ADDRESS);
		String sendMobile = session.getKVParam(JiLuParams.PHONES);
		return session.getMerchant().addMerchantSendInfo(sendName,sendAddress,sendMobile);
	}

}
