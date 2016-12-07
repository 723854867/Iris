package org.Iris.app.jilu.service.realm.user;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {
	
	private static final Logger logger = LoggerFactory.getLogger(User.class);

	private MemMerchant memUser;
	
	private String lock;
	
	public User(MemMerchant memUser) {
		this.memUser = memUser;
		this.lock = RedisKeyGenerator.getUserLockKey(memUser.getMerchat_id());
	}
	
	public long getMerchat_id() { 
		return memUser.getMerchat_id();
	}
	
	public String tryLock() { 
		return Beans.distributeLock.tryLock(lock);
	}
	
	public boolean unLock(String lockId) {
		boolean flag = Beans.distributeLock.unLock(lock, lockId);
		if (!flag)
			logger.warn("User lock {} release failure for lockId {}!", lock, lockId);
		return flag;
	}
}
