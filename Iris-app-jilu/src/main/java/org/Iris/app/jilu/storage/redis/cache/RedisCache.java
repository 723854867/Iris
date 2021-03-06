package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.redis.RedisHashBean;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.util.reflect.BeanUtils;

public abstract class RedisCache {
	
	@Resource
	protected RedisOperate redisOperate;
	
	protected <T extends RedisHashBean> T getHashBean(T bean) {
		return redisOperate.hgetAll(bean.redisKey(), bean);
	}

	public void flushHashBean(RedisHashBean bean) { 
		redisOperate.hmset(bean.redisKey(), BeanUtils.beanToMap(bean));
	}
}
