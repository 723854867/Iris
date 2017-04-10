package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 微信支付统一下单
 * @author liusiyuan
 * 2017年3月6日
 */
public class UNIFIED_ORDER extends SerialMerchantAction{
	@Override
	protected String execute0(MerchantSession session) {
		String outtradeno = session.getKVParam(JiLuParams.OUTTRADENO);
		String body = session.getKVParam(JiLuParams.BODY);
		float price = session.getKVParam(JiLuParams.PRICE);
		return session.getMerchant().orderPay(outtradeno, price, session.getRequest().getRemoteAddr(),body);
	}
}
