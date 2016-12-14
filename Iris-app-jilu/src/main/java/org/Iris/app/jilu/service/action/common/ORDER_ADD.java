package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.util.common.uuid.JdkIdGenerator;

/**
 * 创建订单
 * @author 樊水东
 * 2016年12月14日
 */
public class ORDER_ADD extends CommonAction{

	@Override
	protected String execute0(IrisSession session) {
		String receiveId = session.getKVParam(JiLuParams.RECEIVEId);
		String goodsList = session.getKVParam(JiLuParams.GOODSLIST);
		return null;
	}
}
