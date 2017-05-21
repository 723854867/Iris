package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

public class GOODS_INFO extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		return merchantWebService.getGoodsInfo(goodsId);
	}

}
