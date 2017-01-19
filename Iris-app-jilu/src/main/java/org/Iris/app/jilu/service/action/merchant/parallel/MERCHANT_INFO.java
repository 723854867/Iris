package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 查询商户信息接口
 * @author 樊水东
 * 2016年12月13日
 */
public class MERCHANT_INFO extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		return session.getMerchant().getMerchantInfo();
	}
}
