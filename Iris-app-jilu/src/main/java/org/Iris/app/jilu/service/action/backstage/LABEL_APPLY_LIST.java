package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class LABEL_APPLY_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		return backstageService.labelApplyList(page, pageSize);
	}

}
