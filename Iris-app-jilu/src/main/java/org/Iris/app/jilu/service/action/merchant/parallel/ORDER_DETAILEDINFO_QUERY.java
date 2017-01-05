package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemOrder;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.exception.IllegalConstException;

/**
 * 订单详细信息查询
 * @author 樊水东
 * 2017年1月5日
 */
public class ORDER_DETAILEDINFO_QUERY extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String orderId = session.getKVParam(JiLuParams.ORDERID);
		Merchant merchant = session.getMerchant();
		MemOrder order = merchant.getOrderByOrderId(orderId);
		if(order == null)
			throw IllegalConstException.errorException(JiLuParams.ORDERID);
		
		return merchant.getOrderDetailedInfoByOrderId(orderId);
	}
	
	
}
