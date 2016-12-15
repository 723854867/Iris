package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class ORDER_LOCK extends UnitAction<MerchantSession>{

	public static final ORDER_LOCK INSTANCE = new ORDER_LOCK();
	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Order order = orderCache.getByOrderId(orderId);
		if(order.getStatus()!=1){
			//订单已经确认不能修改
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		}
		order.setStatus(0);
		tx.OrderLock(orderId);
		return Result.jsonSuccess();
	}

}
