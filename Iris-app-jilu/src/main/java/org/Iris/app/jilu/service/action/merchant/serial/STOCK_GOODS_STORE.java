package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 商品进货操作
 * @author fansd
 *
 */
public class STOCK_GOODS_STORE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		long count = session.getKVParam(JiLuParams.COUNT);
		float price = session.getKVParam(JiLuParams.PRICE);
		String memo = session.getKVParamOptional(JiLuParams.MEMO);
		return merchantService.stockGoodsStore(goodsId,count,price,memo,session.getMerchant());
	}

}
