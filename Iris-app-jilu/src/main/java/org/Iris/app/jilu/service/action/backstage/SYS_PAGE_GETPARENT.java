package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class SYS_PAGE_GETPARENT extends BackstageAction{
	@Override
	protected String execute0(IrisSession session) {
		String pagePath = session.getKVParam(JiLuParams.PAGEPATH);
		return backstageService.GetParentPagePath(pagePath);
	}
}
