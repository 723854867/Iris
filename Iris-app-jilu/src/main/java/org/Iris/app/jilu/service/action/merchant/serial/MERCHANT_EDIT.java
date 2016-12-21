package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.Merchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

public class MERCHANT_EDIT extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		
		Merchant memMerchant = session.getUnit().getUnit();
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setUpdated(DateUtils.currentTime());
		unitCache.updateMerchant(memMerchant);
		return Result.jsonSuccess();
	}
}
