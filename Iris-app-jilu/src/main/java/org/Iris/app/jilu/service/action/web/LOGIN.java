package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 商户登陆
 * 
 * @author Ahab
 */
public class LOGIN extends BackstageAction {
	
	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account=null;
		switch (type) {
		case MOBILE:
			account = session.getKVParam(JiLuParams.MOBILE);
			break;
		case EMAIL:
			account = session.getKVParam(JiLuParams.EMAIL);
			break;
		default:
		}
		return commonService.login(type, account, session.getKVParam(JiLuParams.CAPTCHA));
	}
}
