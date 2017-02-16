package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

public class FRIEND_DELETE extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		long targetId = session.getKVParam(JiLuParams.TARGET_ID);
		return relationService.deleteFriend(session.getMerchant(), targetId);
	}
}
