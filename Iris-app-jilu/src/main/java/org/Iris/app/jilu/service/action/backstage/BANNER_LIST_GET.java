package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class BANNER_LIST_GET extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pagesize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String title = session.getKVParam(JiLuParams.TITLE);
		return backstageService.getBannerList(page,pagesize,title);
	}

}
