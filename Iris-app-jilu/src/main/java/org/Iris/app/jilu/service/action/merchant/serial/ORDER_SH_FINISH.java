package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 订单完成售后操作
 * @author fansd
 *
 */
public class ORDER_SH_FINISH extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		String shMemo = session.getKVParam(JiLuParams.MEMO);
		return merchantService.finishOrderSh(orderId,shMemo,session.getMerchant());
	}

}
