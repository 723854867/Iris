package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

public class GOODS_EDIT extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		String zhName = session.getKVParamOptional(JiLuParams.ZH_NAME);
		String goodsFormat = session.getKVParamOptional(JiLuParams.GOODS_FORMAT);
		String usName = session.getKVParamOptional(JiLuParams.US_NAME);
		String classification = session.getKVParamOptional(JiLuParams.CLASSIFICATION);
		String zhBrand = session.getKVParamOptional(JiLuParams.ZH_BRAND);
		String usBrand = session.getKVParamOptional(JiLuParams.US_BRAND);
		String unit = session.getKVParamOptional(JiLuParams.UNIT);
		long weight = session.getKVParamOptional(JiLuParams.WEIGHT);
		String alias = session.getKVParamOptional(JiLuParams.ALIAS);
		String sku = session.getKVParamOptional(JiLuParams.SKU);
		String barcode = session.getKVParamOptional(JiLuParams.BARCODE);
		long unitPrice = session.getKVParamOptional(JiLuParams.UNITPRICE);
		return merchantWebService.updateGoods(goodsId, zhName, usName, goodsFormat, classification, zhBrand, usBrand, unit,weight, alias, barcode, sku, unitPrice);

	}
	
}
