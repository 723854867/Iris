package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
/**
 * 查询商户信息接口
 * @author 樊水东
 * 2016年12月13日
 */
public class MERCHANT_QUERY extends CommonAction{

	public static final MERCHANT_QUERY INSTANCE = new MERCHANT_QUERY();
	@Override
	protected String execute0(IrisSession session) {
		String token = session.getKVParam(JiLuParams.TOKEN);
		String uid = redisOperate.get(RedisKeyGenerator.getTokenUidKey(token));
		if(uid==null || "".equals(uid))
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		MemMerchant memMerchant = tx.queryMemMerchantById(Long.valueOf(uid));
		return Result.jsonSuccess(memMerchant);
	}

}
