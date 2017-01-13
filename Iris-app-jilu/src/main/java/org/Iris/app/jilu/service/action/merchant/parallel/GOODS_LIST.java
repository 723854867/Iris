package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.enums.GoodsListType;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 产品列表
 * @author 樊水东
 * 2017年1月13日
 */
public class GOODS_LIST extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		GoodsListType goodsListType = GoodsListType.match(session.getKVParamOptional(JiLuParams.TYPE)); 
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String name = session.getKVParamOptional(JiLuParams.NAME);
		Merchant merchant = session.getMerchant();
		return merchant.getGoodsList(page, pageSize,name,goodsListType);
	}
	
}
