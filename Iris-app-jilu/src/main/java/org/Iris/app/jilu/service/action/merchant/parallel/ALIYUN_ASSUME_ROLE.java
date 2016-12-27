package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class ALIYUN_ASSUME_ROLE extends ParallelMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		return Result.jsonSuccess(merchant.assumeRole());
	}
}
