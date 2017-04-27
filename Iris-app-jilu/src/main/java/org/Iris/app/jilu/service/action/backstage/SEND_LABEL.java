package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 后台操作发货（标签）
 * @author 樊水东
 * 2017年4月27日
 */
public class SEND_LABEL extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		return backstageService.sendLabel(id);
	}

}
