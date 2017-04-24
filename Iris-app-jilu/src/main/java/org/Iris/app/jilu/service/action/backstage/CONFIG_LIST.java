package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class CONFIG_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		return Result.jsonSuccess(bgConfigMapper.list());
	}

}
