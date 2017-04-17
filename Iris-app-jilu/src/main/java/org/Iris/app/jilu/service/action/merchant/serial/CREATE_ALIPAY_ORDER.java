package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.util.common.OrderNumberUtil;

/**
 * 创建支付宝支付订单
 * @author 樊水东
 * 2017年3月6日
 */
public class CREATE_ALIPAY_ORDER extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String body = session.getKVParam(JiLuParams.BODY);
		String subject = session.getKVParam(JiLuParams.SUBJECT);
		float totalAmount = session.getKVParam(JiLuParams.TOTALAMOUNT);
		return session.getMerchant().createAlipayOrder(body,subject,OrderNumberUtil.getRandomOrderId(1),totalAmount);
	}

}
