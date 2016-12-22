package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.storage.domain.MerchantOrder;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;

public class ORDER_EDIT extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String addGoodsList = session.getKVParamOptional(JiLuParams.ADDGOODSLIST);
		String updateGoodsList = session.getKVParamOptional(JiLuParams.UPDATEGOODSLIST);
		String deleteGoodsList = session.getKVParamOptional(JiLuParams.DELETEGOODSLIST);
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		MerchantOrder order = orderCache.getByOrderId(orderId);
		int status  = order.getStatus();
		if(status!=0){
			//订单已经确认不能修改
			return Result.jsonError(JiLuCode.ORDER_IS_LOCK);
		}
		MerchantCustomer customer = unitCache.getMemCustomerById(customerId);
		if(customer == null)
			throw IllegalConstException.errorException(JiLuParams.CUSTOMER_ID);
		order.setCustomerAddress(customer.getAddress());
		order.setCustomerId(customerId);
		order.setCustomerName(customer.getName());
		order.setCustomerMobile(customer.getMobile());
		orderCache.updateOrder(order, addGoodsList, updateGoodsList, deleteGoodsList);
		return Result.jsonSuccess();
	}
}