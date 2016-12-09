package org.Iris.app.jilu.service.realm.unit.merchant;

import org.Iris.app.jilu.service.realm.unit.UnitAdapter;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.common.IrisSecurity;

/**
 * 商户
 * 
 * @author Ahab
 */
public class Merchant extends UnitAdapter<MemMerchant> {
	
	public Merchant(MemMerchant merchant) {
		super(merchant);
	}
	
	/**
	 * 商户登陆动作
	 * 
	 */
	public void login(String account) {
		// 删除老的 token
		if (null != unit.getToken()) 
			redisOperate.del(RedisKeyGenerator.getTokenUidKey(unit.getToken()));
		
		// 生成新的 token 并且写入
		String token = IrisSecurity.encodeToken(account);
		unit.setToken(token);
		unitCache.flushHashBean(unit);
		redisOperate.set(RedisKeyGenerator.getTokenUidKey(unit.getToken()), String.valueOf(unit.getMerchantId()));
	}
}
