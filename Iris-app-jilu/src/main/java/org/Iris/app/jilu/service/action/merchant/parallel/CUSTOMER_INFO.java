package org.Iris.app.jilu.service.action.merchant.parallel;

import org.Iris.app.jilu.common.bean.form.CustomerForm;
import org.Iris.app.jilu.service.action.merchant.ParallelMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;

/**
 * 客户信息接口
 * 
 * @author Ahab
 */
public class CUSTOMER_INFO extends ParallelMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		Merchant merchant = session.getMerchant();
		MemCustomer customer = merchant.getCustomer(customerId);
		if (null == customer)
			return Result.jsonError(JiLuCode.CUSTOMER_NOT_EXIST);
		return Result.jsonSuccess(new CustomerForm(customer));
	}
}
