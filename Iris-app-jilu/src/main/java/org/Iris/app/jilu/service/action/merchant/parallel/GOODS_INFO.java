package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

public class GOODS_INFO extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		Merchant merchant = session.getMerchant();
		return merchant.getGoodsInfo(goodsId);
	}

}
