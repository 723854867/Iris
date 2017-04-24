package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 版本修改和标记删除
 * @author liusiyuan
 * 2017年4月10日
 */
public class VERSION_UPDATE extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long versionId = session.getKVParam(JiLuParams.ID);
		String versionNum = session.getKVParam(JiLuParams.VERSION_NUM);
		int status = session.getKVParam(JiLuParams.STATUS);
		return backstageService.updateVersion(versionId, versionNum, status);
	}

}
