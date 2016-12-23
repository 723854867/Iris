package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 好友申请
 * 
 * @author Ahab
 */
public class FRIEND_APPLY extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		long targetId = session.getKVParam(JiLuParams.TARGET_ID);
		Merchant applier = session.getUnit().getUnit();
		if (applier.getMerchantId() == targetId)					// 不能给自己发申请
			return Result.jsonError(JiLuCode.SELF_LIMIT);
		MerchantOperator target = unitCache.getMerchantByMerchantId(targetId);
		if (null == target)
			return Result.jsonError(JiLuCode.TARGET_MERCHANT_NOT_EXIST);
		return relationService.apply(applier, target.getUnit(), session.getKVParam(JiLuParams.MEMO));
	}
}
