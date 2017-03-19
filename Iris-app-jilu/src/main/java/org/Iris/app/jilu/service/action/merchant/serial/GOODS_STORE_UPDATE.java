package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

public class GOODS_STORE_UPDATE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		String memo = session.getKVParamOptional(JiLuParams.MEMO);
		long count = session.getKVParam(JiLuParams.COUNT);
		float price = session.getKVParam(JiLuParams.PRICE);
		String operation = session.getKVParam(JiLuParams.OPERATION);
		Merchant merchant = session.getMerchant();
		return merchantService.updateGoodsStore(goodsId,memo,count,price,operation,merchant);
	}

}
