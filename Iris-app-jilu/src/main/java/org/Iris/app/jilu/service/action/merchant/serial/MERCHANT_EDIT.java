package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

public class MERCHANT_EDIT extends SerialMerchantAction {
	
	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		
		MemMerchant merchant = session.getUnit().getUnit();
		merchant.setAddress(address);
		merchant.setName(name);
		merchant.setUpdated(DateUtils.currentTime());
		unitCache.updateMerchant(merchant);
		return Result.jsonSuccess();
	}
}
