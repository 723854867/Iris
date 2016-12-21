package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class LOGOUT extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		redisOperate.del(CommonKeyGenerator.tokenMerchantIdKey(session.getUnit().getUnit().getToken()));
		return Result.jsonSuccess();
	}
}
