package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.Random;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.SerializeUtil;

/**
 * 创建订单
 * @author 樊水东
 * 2016年12月14日
 */
public class ORDER_ADD extends ParallelMerchantAction {
	
	public static final ORDER_ADD INSTANCE						 = new ORDER_ADD();

	@Override
	protected String execute0(MerchantSession session) {
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		long customerId = session.getKVParam(JiLuParams.CUSTOMERID);
		String orderId = System.currentTimeMillis()+new Random().nextInt(10)+"";
		MemOrderGoods orderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MemOrderGoods[].class);
		if(orderGoods==null || orderGoods.length==0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		MemMerchant memMerchant = session.getUnit().getUnit();
		MemCustomer customer = unitCache.getMemCustomerById(customerId);
		if(customer == null)
			throw IllegalConstException.errorException(JiLuParams.CUSTOMERID);
		MemOrder order = BeanCreator.newMemOrder(orderId, memMerchant.getMerchantId(), memMerchant.getName(), 
				customerId, customer.getName(), customer.getMobile(), customer.getAddress(),0);
		orderCache.createOrder(order, orderGoods);
		return Result.jsonSuccess(order);
	}
}
