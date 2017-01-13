package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 产品库存列表
 * @author 樊水东
 * 2017年1月13日
 */
public class GOODS_STORE_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		Merchant merchant = session.getMerchant();
		return merchant.getGoodsStoreList(page, pageSize);
	}

	
}
