package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

public class LOGOUT extends UnitAction<MerchantSession> {
	
	public static final LOGOUT INSTANCE = new LOGOUT();

	@Override
	protected String execute0(MerchantSession session) {
		redisOperate.del(RedisKeyGenerator.getTokenUidKey(session.getUnit().getUnit().getToken()));
		return Result.jsonSuccess();
	}
}
