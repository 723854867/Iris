package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 删除版本
 * @author liusiyuan
 * 2017年4月11日
 */
public class VERSION_DEL extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long id = session.getKVParam(JiLuParams.ID);
		return backstageService.delVersion(id);
	}

}
