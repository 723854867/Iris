package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 获取微信接口调用凭证
 * @author 樊水东
 * 2017年2月14日
 */
public class ACCESSTOKEN_GET extends CommonAction{

	@Override
	protected String execute0(IrisSession session) {
		String code = session.getKVParam(JiLuParams.CODE);
		return null;
	}

}
