package org.Iris.app.jilu.service.realm.unit.merchant;

import org.Iris.app.jilu.service.realm.unit.UnitOperator;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.lang.DateUtils;

/**
 * 商户
 * 
 * @author Ahab
 */
public class MerchantOperator extends UnitOperator<MemMerchant> {
	
	public MerchantOperator(MemMerchant merchant) {
		super(merchant);
	}
	
	/**
	 * 商户登陆动作
	 * 
	 */
	public boolean login(String account, boolean create) {
		String lockId = tryLock();
		if (null == lockId) 
			return false;
		
		try {
			// 删除老的 token
			if (null != unit.getToken()) 
				redisOperate.del(CommonKeyGenerator.tokenMerchantIdKey(unit.getToken()));
			
			// 生成新的 token 并且写入
			String token = IrisSecurity.encodeToken(account);
			unit.setToken(token);
			// 如果是正常的登录(创建用户也会调用  login)，则还需要更新最近一次登录时间
			if (!create) {
				unit.setLastLoginTime(DateUtils.currentTime());
				unitCache.updateMerchant(unit);
			} else 
				unitCache.flushHashBean(unit);
			redisOperate.set(CommonKeyGenerator.tokenMerchantIdKey(unit.getToken()), String.valueOf(unit.getMerchantId()));
			return true;
		} finally {
			unLock(lockId);
		}
	}
}
