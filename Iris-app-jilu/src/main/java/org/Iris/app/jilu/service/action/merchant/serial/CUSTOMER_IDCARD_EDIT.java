package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

public class CUSTOMER_IDCARD_EDIT extends SerialMerchantAction {

	@Override
	protected String execute0(MerchantSession session) {
		long customerId = session.getKVParam(JiLuParams.CUSTOMER_ID);
		int mod = session. getKVParam(JiLuParams.MOD);
		Merchant merchant = session.getMerchant();
		return merchant.editCustomerIdcard(customerId, mod);
	}
}
