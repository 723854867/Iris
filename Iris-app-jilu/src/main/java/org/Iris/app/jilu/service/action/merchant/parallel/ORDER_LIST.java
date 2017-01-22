package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 显示自己的所有订单
 * @author 樊水东
 * 2017年1月22日
 */
public class ORDER_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		return merchant.getMyOrderList();
	}

	
}
