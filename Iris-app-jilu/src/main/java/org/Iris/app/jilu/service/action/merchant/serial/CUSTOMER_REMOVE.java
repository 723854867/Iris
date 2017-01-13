package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 删除联系人
 * @author 樊水东
 * 2017年1月13日
 */
public class CUSTOMER_REMOVE extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		Merchant merchant = session.getMerchant();
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		return merchant.removeCustomer(customerId);
	}

}
