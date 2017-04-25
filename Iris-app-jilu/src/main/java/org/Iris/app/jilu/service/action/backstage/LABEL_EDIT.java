package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class LABEL_EDIT extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long id  = session.getKVParam(JiLuParams.ID);
		String labelNum = session.getKVParam(JiLuParams.LABELNUM);
		float price = session.getKVParam(JiLuParams.PRICE);
		return backstageService.updateLabel(id, labelNum, price);
	}

}