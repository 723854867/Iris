package org.Iris.app.jilu.storage.mybatis.domain;

import org.Iris.redis.RedisHashBean;

public class MemMerchant implements RedisHashBean {
	
	private long uid;
	
	public long getUid() {
		return uid;
	}

	@Override
	public String redisKey() {
		return null;
	}
}
