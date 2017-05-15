package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 吉币日志 主要包含 三个部分 创建订单 购买标签 吉币充值
 * @author 樊水东
 * 2017年5月15日
 */
public class JB_CZ_LOG extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		int startTime = session.getKVParamOptional(JiLuParams.START);
		int endTime = session.getKVParamOptional(JiLuParams.END);
		return backstageService.getJbCzLog(page,pageSize,startTime,endTime);
	}

}
