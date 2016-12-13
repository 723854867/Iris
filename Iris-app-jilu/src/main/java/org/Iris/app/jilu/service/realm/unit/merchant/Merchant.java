package org.Iris.app.jilu.service.realm.unit.merchant;

import org.Iris.app.jilu.service.realm.unit.UnitAdapter;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.lang.DateUtils;

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
	public boolean login(String account) {
		String lockId = tryLock();
		if (null == lockId) 
			return false;
		
		try {
			// 删除老的 token
			if (null != unit.getToken()) 
				redisOperate.del(RedisKeyGenerator.getTokenUidKey(unit.getToken()));
			
			// 生成新的 token 并且写入
			String token = IrisSecurity.encodeToken(account);
			unit.setToken(token);
			unit.setLastLoginTime(DateUtils.currentTime());
			unitCache.flushHashBean(unit);
			redisOperate.set(RedisKeyGenerator.getTokenUidKey(unit.getToken()), String.valueOf(unit.getMerchantId()));
			return true;
		} finally {
			unLock(lockId);
		}
	}
}
