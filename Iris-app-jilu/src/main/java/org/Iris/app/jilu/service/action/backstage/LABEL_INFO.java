package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

public class LABEL_INFO extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		return Result.jsonSuccess(backstageService.getLabel(id));
	}

}
