package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 搜索产品库存
 * @author 樊水东
 * 2017年2月28日
 */
public class GOODS_STORE_SEARCH extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		int type = session.getKVParam(JiLuParams.TYPE);
		String value = session.getKVParam(JiLuParams.VALUE);
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		return merchant.searchGoodsStore(type,value,page,pageSize);
	}

}
