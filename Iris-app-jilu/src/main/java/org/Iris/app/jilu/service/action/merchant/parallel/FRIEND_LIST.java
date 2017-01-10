package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class FRIEND_LIST extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		Pager<PubRelation> pager = relationService.friendList(session.getMerchant(), page, pageSize);
		return Result.jsonSuccess(pager);
	}
}
