package org.Iris.app.jilu.service.realm.user;

import org.Iris.app.jilu.model.AccountType;
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.cache.UserCache;

public class UserService {
	
	private UserCache userCache;

	/**
	 * 用户登录
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public User login(AccountType type, String account) { 
		MemMerchant merchant = userCache.getMerchantByAccount(type, account);
		if (null == merchant) 
			return null;
		
		return null;
	}
	
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
}
