package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class LOGIN extends BackstageAction {

	@Override
	protected String execute0(IrisSession session) {
		String account = session.getKVParam(JiLuParams.ACCOUNT);
		String password = session.getKVParam(JiLuParams.PASSWORD);


		return backstageService.login(account, password,session.getRequest());
	}

}
