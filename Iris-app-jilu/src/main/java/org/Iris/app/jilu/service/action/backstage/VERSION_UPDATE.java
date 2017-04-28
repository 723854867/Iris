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
		int delFlag = session.getKVParam(JiLuParams.DELFLAG);
		String content = session.getKVParam(JiLuParams.CONTENT);
		String downloadUrl = session.getKVParam(JiLuParams.DOWNLOADURL);
		int operatSys = session.getKVParam(JiLuParams.OPERATSYS);
		return backstageService.updateVersion(versionId, versionNum, status, delFlag, content, downloadUrl, operatSys);
	}

}
