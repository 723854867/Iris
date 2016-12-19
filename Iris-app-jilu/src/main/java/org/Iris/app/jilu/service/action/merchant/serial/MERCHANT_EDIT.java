package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

public class MERCHANT_EDIT extends UnitAction<MerchantSession> {
	
	public static final MERCHANT_EDIT INSTANCE						 = new MERCHANT_EDIT();
	
	private MERCHANT_EDIT() {}

	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		
		MemMerchant memMerchant = session.getUnit().getUnit();
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setUpdated(DateUtils.currentTime());
		unitCache.updateMerchant(memMerchant);
		return Result.jsonSuccess();
	}
}