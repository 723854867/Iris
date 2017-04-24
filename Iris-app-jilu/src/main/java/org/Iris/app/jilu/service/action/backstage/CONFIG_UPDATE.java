package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class CONFIG_UPDATE extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String key = session.getKVParam(JiLuParams.KEY);
		String value = session.getKVParam(JiLuParams.VALUE);
		return backstageService.updateConfig(key,value);
	}

}
