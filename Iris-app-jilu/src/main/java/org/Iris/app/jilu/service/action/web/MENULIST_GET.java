package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.session.MerchantWebSession;

public class MENULIST_GET extends MerchantWebAction{
	@Override
	protected String execute0(MerchantWebSession session) {
		return merchantWebService.getMenuList();
	}
}
