package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 后台产品列表
 * @author 樊水东
 * 2017年1月13日
 */
public class MERCHANT_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String name = session.getKVParamOptional(JiLuParams.NAME);
		return backstageService.getMerchantList(page, pageSize,name);
	}
	
}
