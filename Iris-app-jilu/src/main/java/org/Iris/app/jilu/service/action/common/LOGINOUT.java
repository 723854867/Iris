package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;

public class LOGINOUT extends CommonAction{
	
	public static final LOGINOUT INSTANCE = new LOGINOUT();
	
	@Override
	protected String execute0(IrisSession session) {
		String token = session.getKVParam(JiLuParams.TOKEN);
		if(!luaOperate.delIfExist(RedisKeyGenerator.getTokenUidKey(token)))
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		return Result.jsonSuccess();
	}

}
