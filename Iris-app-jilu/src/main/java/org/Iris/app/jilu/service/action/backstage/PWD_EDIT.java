package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class PWD_EDIT extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String account = session.getKVParam(JiLuParams.ACCOUNT);
		String oldPwd = session.getKVParam(JiLuParams.OLDPWD);
		String newPwd = session.getKVParam(JiLuParams.NEWPWD);
		
		return backstageService.updatePwd(account,oldPwd,newPwd);
	}

}
