package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 版本添加
 * @author liusiyuan
 * 2017年4月7日
 */
public class VERSION_ADD extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		String versionNum = session.getKVParam(JiLuParams.VERSION_NUM);
		int status = session.getKVParam(JiLuParams.STATUS);
		String content = session.getKVParamOptional(JiLuParams.CONTENT);
		String downloadUrl = session.getKVParam(JiLuParams.DOWNLOADURL);
		int operatSys = session.getKVParam(JiLuParams.OPERATSYS);
		return backstageService.addVersion(versionNum,status,content,downloadUrl,operatSys);
	}

}
