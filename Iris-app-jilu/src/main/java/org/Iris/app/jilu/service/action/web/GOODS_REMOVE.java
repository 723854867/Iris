package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

/**
 * 删除商品
 * @author 樊水东
 * 2017年1月14日
 */
public class GOODS_REMOVE extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		return merchantWebService.removeGoods(goodsId);
	}
	
	
}
