package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.session.IrisSession;

public class MENULIST_GET extends BackstageAction{
	@Override
	protected String execute0(IrisSession session) {
		return merchantWebService.getMenuList();
	}
}
