package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.common.BeanCreator;
import org.Iris.app.jilu.common.bean.form.CustomerForm;
import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MerchantCustomer;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 商户添加客户
 * 
 * @author ahab
 */
public class CUSTOMER_ADD extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String mobile = session.getKVParam(JiLuParams.MOBILE);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String memo = session.getKVParam(JiLuParams.MEMO);
		String IDNumber = session.getKVParam(JiLuParams.ID_NUMBER);
		MerchantCustomer customer = BeanCreator.newMemCustomer(session.getUnit().uid(), name, mobile, address, memo, IDNumber);
		unitCache.insertCustomer(customer);
		return Result.jsonSuccess(new CustomerForm(customer));
	}
}