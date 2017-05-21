package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantWebSession;

public class SYS_PAGE_GETPARENT extends MerchantWebAction{
	@Override
	protected String execute0(MerchantWebSession session) {
		String pagePath = session.getKVParam(JiLuParams.PAGEPATH);
		return merchantWebService.GetParentPagePath(pagePath);
	}
}
