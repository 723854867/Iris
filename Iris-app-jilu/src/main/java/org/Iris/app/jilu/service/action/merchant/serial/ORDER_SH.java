package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 订单售后操作
 * @author fansd
 *
 */
public class ORDER_SH extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		String shInfo = session.getKVParam(JiLuParams.INFO);
		int shTime = session.getKVParam(JiLuParams.TIME);
		return merchantService.orderSh(orderId,shInfo,shTime,session.getMerchant());
	}

}
