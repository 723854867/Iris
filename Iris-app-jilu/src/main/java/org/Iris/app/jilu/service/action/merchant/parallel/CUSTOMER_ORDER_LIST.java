package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 商家与此联系人所有订单基本信息列表
 * @author 樊水东
 * 2017年1月22日
 */
public class CUSTOMER_ORDER_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		Merchant merchant = session.getMerchant();
		return merchant.getCustomerOrderList(page,pageSize,customerId);
	}

	
}
