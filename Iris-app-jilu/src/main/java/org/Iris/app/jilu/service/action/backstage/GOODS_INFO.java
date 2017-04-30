package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;

public class GOODS_INFO extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		long goodsId = session.getKVParam(JiLuParams.GOODS_ID);
		return backstageService.getGoodsInfo(goodsId);
	}

}
