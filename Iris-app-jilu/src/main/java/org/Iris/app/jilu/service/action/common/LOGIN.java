package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 商户登陆
 * 
 * @author Ahab
 */
public class LOGIN extends CommonAction {
	
	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);
		return commonservice.login(type, account, session.getKVParam(JiLuParams.CAPTCHA));
	}
}
