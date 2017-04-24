package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 所有版本获取
 * @author liusiyuan
 * 2017年4月10日
 */
public class VERSION_GET extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		return backstageService.versionGet();
	}

}
