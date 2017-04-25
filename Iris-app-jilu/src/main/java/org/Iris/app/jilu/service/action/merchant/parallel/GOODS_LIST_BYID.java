package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 通过商品id查找商品列表
 * @author 樊水东
 * 2017年3月13日
 */
public class GOODS_LIST_BYID extends ParallelMerchantAction{
	@Override
	protected String execute0(MerchantSession session) {
		String ids = session.getKVParam(JiLuParams.IDS);//code可以是多个格式如下 id;id;id
		return session.getMerchant().getGoodsListByGoodsId(ids);
	}

	
}