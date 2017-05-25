package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.SysUser;
import org.Iris.app.jilu.web.session.IrisSession;

public class MENULIST_GET extends BackstageAction{
	@Override
	protected String execute0(IrisSession session) {
		SysUser sysUser = (SysUser)session.getRequest().getSession().getAttribute("user");
		return backstageService.GetMenuList(sysUser);
	}
}
