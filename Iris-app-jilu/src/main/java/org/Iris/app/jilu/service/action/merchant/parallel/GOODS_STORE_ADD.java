package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 添加产品仓库
 * @author 樊水东
 * 2017年2月16日
 */
public class GOODS_STORE_ADD extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		String goodsCode = session.getKVParam(JiLuParams.GOODS_CODE);
		return null;
	}

	
}
