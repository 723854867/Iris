package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 获取最新版本
 * @author liusiyuan
 * 2017年4月10日
 */
public class VERSION_GET extends CommonAction{

	@Override
	protected String execute0(IrisSession session) {
		return commonService.versionGet();
	}

}
