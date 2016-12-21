package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.common.bean.enums.CustomerListType;
import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 商户客户列表
 * 
 * @author Ahab
 */
public class CUSTOMER_LIST extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		CustomerListType type = CustomerListType.match(session.getKVParamOptional(JiLuParams.TYPE)); 
		int page = session.getKVParam(JiLuParams.PAGE);
		int pageSize = session.getKVParam(JiLuParams.PAGE_SIZE);
		return Result.jsonSuccess(unitCache.getCustomerList(type, session.getUnit().uid(), page, pageSize));
	}
}
