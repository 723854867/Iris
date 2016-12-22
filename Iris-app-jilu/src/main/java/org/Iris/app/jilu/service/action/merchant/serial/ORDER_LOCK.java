package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class ORDER_LOCK extends SerialMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		MerchantOrder order = orderCache.getByOrderId(orderId);
		if(order.getStatus()!=0){
			//订单已经完成
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		}
		order.setStatus(1);
		orderCache.orderLock(order);
		return Result.jsonSuccess();
	}

}
