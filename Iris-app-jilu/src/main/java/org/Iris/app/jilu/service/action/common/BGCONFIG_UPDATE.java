package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 后台配置文件修改
 * @author 樊水东
 * 2017年2月17日
 */
public class BGCONFIG_UPDATE extends CommonAction{

	@Override
	protected String execute0(IrisSession session) {
		String key = session.getKVParam(JiLuParams.KEY);
		String value = session.getKVParam(JiLuParams.VALUE);
		return commonService.updateBgConfig(key, value);
	}

}
