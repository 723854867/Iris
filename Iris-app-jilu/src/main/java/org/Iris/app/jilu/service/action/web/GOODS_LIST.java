package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

/**
 * 后台产品列表
 * @author 樊水东
 * 2017年1月13日
 */
public class GOODS_LIST extends MerchantWebAction{

	@Override
	protected String execute0(MerchantWebSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String zhName = session.getKVParamOptional(JiLuParams.ZH_NAME);
		String alias = session.getKVParamOptional(JiLuParams.ALIAS);
		String goodsCode = session.getKVParamOptional(JiLuParams.GOODS_CODE);
		return merchantWebService.getGoodsList(page, pageSize,zhName,alias,goodsCode,session.getMemMerchant());
	}
	
}
