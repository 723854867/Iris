package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.domain.Order;
import org.Iris.app.jilu.storage.domain.OrderGoods;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;
import org.Iris.util.common.uuid.JdkIdGenerator;

/**
 * 创建订单
 * @author 樊水东
 * 2016年12月14日
 */
public class ORDER_ADD extends UnitAction<MerchantSession>{
	
	public static final ORDER_ADD INSTANCE						 = new ORDER_ADD();

	@Override
	protected String execute0(MerchantSession session) {
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		String receiveId = session.getKVParam(JiLuParams.RECEIVEId);
		String orderId = new JdkIdGenerator().generateId().toString().replaceAll("-", "");
		OrderGoods orderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(goodsList, OrderGoods[].class);
		if(orderGoods==null || orderGoods.length==0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		Order order = new Order(orderId, receiveId, "test");
		order.setStatus(1);
		orderCache.createOrder(order, orderGoods);
		return Result.jsonSuccess(order);
	}
}
