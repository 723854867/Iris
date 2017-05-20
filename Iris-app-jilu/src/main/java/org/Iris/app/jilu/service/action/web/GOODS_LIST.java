package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

/**
 * 后台产品列表
 * @author 樊水东
 * 2017年1月13日
 */
public class GOODS_LIST extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		String zhName = session.getKVParamOptional(JiLuParams.ZH_NAME);
		String alias = session.getKVParamOptional(JiLuParams.ALIAS);
		String goodsCode = session.getKVParamOptional(JiLuParams.GOODS_CODE);
		MemMerchant memMerchant = (MemMerchant)session.getRequest().getSession().getAttribute("merchant");
		if(memMerchant ==null)
			return Result.jsonError(ICode.Code.TOKEN_INVALID);
		return merchantWebService.getGoodsList(page, pageSize,zhName,alias,goodsCode,memMerchant);
	}
	
}
