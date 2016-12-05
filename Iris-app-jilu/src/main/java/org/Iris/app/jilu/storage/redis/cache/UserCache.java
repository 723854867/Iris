package org.Iris.app.jilu.storage.redis.cache;

import org.Iris.app.jilu.model.AccountType;
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.util.reflect.BeanUtils;

/**
 * 负责所有用户相关的数据存取：比如 mem_merchant,mem_customer等
 * 
 * @author Ahab
 */
public class UserCache {

	private MemMerchantMapper memUserMapper;
	private RedisOperate redisOperate;

	public MemMerchant getById(long uid) { 
		MemMerchant memUser = redisOperate.hgetAll(RedisKeyGenerator.getMemMerchantDataKey(uid), new MemMerchant());
		if (null != memUser)
			return memUser;
		memUser = memUserMapper.getById(uid);
		if (null == memUser)
			return null;
		_flushCache(memUser);
		return memUser;
	}

	public MemMerchant getMerchantByAccount(AccountType type, String account) {
		
		return null;
	}

	public void insert(MemMerchant memUser) {
		
	}
	
	private void _flushCache(MemMerchant memUser) { 
		redisOperate.hmset(RedisKeyGenerator.getMemMerchantDataKey(memUser.getUid()), BeanUtils.beanToMap(memUser));
	}
}
