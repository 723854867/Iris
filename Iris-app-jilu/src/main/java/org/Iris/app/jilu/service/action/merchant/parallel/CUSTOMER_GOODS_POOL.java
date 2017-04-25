package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 此联系人在商家里购买的商品汇总记录
 * @author 樊水东
 * 2017年4月25日
 */
public class CUSTOMER_GOODS_POOL extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		Merchant merchant = session.getMerchant();
		return merchant.getCustomerGoodsPool(page,pageSize,customerId);
	}

}
