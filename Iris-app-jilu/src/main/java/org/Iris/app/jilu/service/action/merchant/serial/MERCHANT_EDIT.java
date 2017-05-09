package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

public class MERCHANT_EDIT extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		Merchant merchant = session.getMerchant();
		return merchant.editInfo(name, address);
	}
}