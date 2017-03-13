package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 通过条形码查找商品列表
 * @author 樊水东
 * 2017年3月13日
 */
public class GOODS_LIST_BYCODE extends ParallelMerchantAction{
	@Override
	protected String execute0(MerchantSession session) {
		String code = session.getKVParam(JiLuParams.CODE);//code可以是多个格式如下 code1;code2;code3
		return session.getMerchant().getGoodsListByCode(code);
	}

	
}
