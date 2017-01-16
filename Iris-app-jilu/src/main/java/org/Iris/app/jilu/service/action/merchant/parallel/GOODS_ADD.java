package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 商户添加产品
 * 
 * @author fansd
 */
public class GOODS_ADD extends ParallelMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String goodsCode = session.getKVParam(JiLuParams.GOODS_CODE);
		String zhName = session.getKVParam(JiLuParams.ZH_NAME);
		String goodsFormat = session.getKVParam(JiLuParams.GOODS_FORMAT);
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
		Merchant merchant = session.getMerchant();
		return merchantService.insertGoods(BeanCreator.newMemGoods(goodsCode, zhName, usName, goodsFormat, classification, zhBrand, 
				usBrand, unit, weight, alias, barcode, sku,unitPrice,merchant),merchant);
	}
}
