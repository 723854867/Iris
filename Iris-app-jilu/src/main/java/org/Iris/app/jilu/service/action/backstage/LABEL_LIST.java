package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 查找标签列表
 * @author 樊水东
 * 2017年4月27日
 */
public class LABEL_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		int status = session.getKVParam(JiLuParams.STATUS);
		return backstageService.labelApplyList(page, pageSize,status);
	}

}
