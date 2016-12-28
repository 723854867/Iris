package org.Iris.app.jilu.service.action.merchant.parallel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
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
		Merchant merchant = session.getMerchant();
		List<MemOrderGoods> list = new ArrayList<MemOrderGoods>(Arrays.asList(SerializeUtil.JsonUtil.GSON.fromJson(goodsList, MemOrderGoods[].class)));
		if(list==null || list.size() == 0)
			throw IllegalConstException.errorException(JiLuParams.GOODSLIST);
		MemCustomer customer = merchant.getCustomer(customerId);
		if(customer == null)
			throw IllegalConstException.errorException(JiLuParams.CUSTOMER_ID);
		return merchant.createOrder(customer, list);
	}
}
