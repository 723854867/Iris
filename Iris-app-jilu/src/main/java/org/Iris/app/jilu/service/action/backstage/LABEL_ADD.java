package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class LABEL_ADD extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String labelNum = session.getKVParam(JiLuParams.LABELNUM);
		float price = session.getKVParam(JiLuParams.PRICE);
		return backstageService.addLabel(labelNum,price);
	}

}
