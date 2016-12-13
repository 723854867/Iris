package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.service.action.UnitAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

public class MERCHANT_UPDATE extends UnitAction<MerchantSession> {
	
	public static final MERCHANT_UPDATE INSTANCE						 = new MERCHANT_UPDATE();
	
	private MERCHANT_UPDATE() {}

	@Override
	protected String execute0(MerchantSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String avatar = session.getKVParam(JiLuParams.AVATAR);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		
		MemMerchant memMerchant = session.getUnit().getUnit();
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setAvatar(avatar);
		memMerchant.setUpdated(DateUtils.currentTime());
		tx.updateMerchant(memMerchant);
		return Result.jsonSuccess();
	}
}
