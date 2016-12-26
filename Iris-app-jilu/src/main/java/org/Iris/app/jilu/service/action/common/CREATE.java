package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;

/**
 * 商户完善信息
 * 
 * @author Ahab
 */
public class CREATE extends CommonAction {
	
	@Override
	protected String execute0(IrisSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String token = session.getKVParam(JiLuParams.TOKEN);
		AccountModel am = luaOperate.delAndGetHash(CommonKeyGenerator.createMarkDataKey(token), new AccountModel());
		if (null == am)
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		return commonservice.create(token, name, address);
	}
}
