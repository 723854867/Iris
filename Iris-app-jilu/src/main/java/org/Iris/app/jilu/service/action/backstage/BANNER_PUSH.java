package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class BANNER_PUSH extends BackstageAction{
	@Override
	protected String execute0(IrisSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		return backstageService.bannerPush(id);
	}
}
