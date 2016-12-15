package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;

public class ORDER_EDIT extends UnitAction<MerchantSession>{
	
	public static final ORDER_EDIT INSTANCE						 = new ORDER_EDIT();

	@Override
	protected String execute0(MerchantSession session) {
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		String receiveId = session.getKVParam(JiLuParams.RECEIVEId);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Order order = orderCache.getByOrderId(orderId);
		int status  = order.getStatus();
		if(status!=1){
			//订单已经确认不能修改
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		}
		OrderGoods orderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(goodsList, OrderGoods[].class);
		order = orderCache.updateOrder(new Order(orderId, receiveId, "test"), orderGoods);
		return Result.jsonSuccess(order);
	}
}