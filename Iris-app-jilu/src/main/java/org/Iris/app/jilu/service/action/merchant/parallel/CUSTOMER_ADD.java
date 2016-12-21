package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 商户添加客户
 * 
 * @author ahab
 */
public class CUSTOMER_ADD extends ParallelMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String mobile = session.getKVParam(JiLuParams.MOBILE);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String memo = session.getKVParam(JiLuParams.MEMO);
		String IDNumber = session.getKVParam(JiLuParams.ID_NUMBER);
		
		unitCache.insertCustomer(BeanCreator.newMemCustomer(session.getUnit().uid(), name, mobile, address, memo, IDNumber));
		return Result.jsonSuccess();
	}
}
