package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
/**
 * 购买标签
 * @author 樊水东
 * 2017年4月25日
 */
public class LABEL_BUY extends ParallelMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		long count = session.getKVParam(JiLuParams.COUNT);
		return session.getMerchant().buyLabel((int)count);
	}

}
