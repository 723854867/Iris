package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.Random;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
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
	
	@Override
	protected String execute0(MerchantSession session) {
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		String orderId = System.currentTimeMillis()+""+new Random().nextInt(10);
		MerchantOrderGoods orderGoods[] = SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MerchantOrderGoods[].class);
		if(orderGoods==null || orderGoods.length==0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		Merchant memMerchant = session.getUnit().getUnit();
		MerchantCustomer customer = unitCache.getMerchantCustomerById(memMerchant.getMerchantId(),customerId);
		if(customer == null)
			throw IllegalConstException.errorException(JiLuParams.CUSTOMER_ID);
		MerchantOrder order = BeanCreator.newMemOrder(orderId, memMerchant.getMerchantId(), memMerchant.getName(), memMerchant.getAddress(),
				customerId, customer.getName(), customer.getMobile(), customer.getAddress(),0);
		orderCache.createOrder(order, orderGoods);
		return Result.jsonSuccess(order);
	}
}
