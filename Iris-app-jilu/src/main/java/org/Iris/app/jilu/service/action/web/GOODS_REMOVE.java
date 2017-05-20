package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 删除商品
 * @author 樊水东
 * 2017年1月14日
 */
public class GOODS_REMOVE extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		return backstageService.removeGoods(goodsId);
	}
	
	
}
