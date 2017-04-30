package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
/**
 * 商户黑名单操作
 * @author Administrator
 *
 */
public class MERCHANT_OPERATION extends BackstageAction{

	@Override
	protected String execute0(IrisSession session) {
		int type = session.getKVParam(JiLuParams.TYPE);
		long merchantId = session.getKVParam(JiLuParams.MERCHANTID);
		return backstageService.merchantOperation(type,merchantId);
	}

}
