package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.lang.DateUtils;

public class MERCHANT_UPDATE extends CommonAction {
	
	public static final MERCHANT_UPDATE INSTANCE						 = new MERCHANT_UPDATE();
	
	private MERCHANT_UPDATE() {}

	@Override
	protected String execute0(IrisSession session) {
		String name = session.getKVParam(JiLuParams.NAME);
		String avatar = session.getKVParam(JiLuParams.AVATAR);
		String address = session.getKVParam(JiLuParams.ADDRESS);
		String token = session.getKVParam(JiLuParams.TOKEN);
		
		String uid = redisOperate.get(RedisKeyGenerator.getTokenUidKey(token));
		if(uid==null || "".equals(uid))
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		MemMerchant memMerchant = tx.queryMemMerchantById(Long.valueOf(uid));
		memMerchant.setAddress(address);
		memMerchant.setName(name);
		memMerchant.setAvatar(avatar);
		memMerchant.setUpdated(DateUtils.currentTime());
		tx.updateMerchant(memMerchant);
		
		return Result.jsonSuccess();
	}
}
