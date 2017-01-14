package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 删除商品
 * @author 樊水东
 * 2017年1月14日
 */
public class GOODS_REMOVE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		Merchant merchant = session.getMerchant();
		return merchant.removeGoods(goodsId);
	}
	
	
}
