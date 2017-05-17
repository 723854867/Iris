package org.Iris.app.jilu.storage.redis;

import org.Iris.app.jilu.common.Beans;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.reflect.BeanUtils;

public abstract class RedisCache implements Beans{
	
//	@Resource
//	protected RedisOperate redisOperate;
	
	public <T extends RedisHashBean> T getHashBean(T bean) {
		return redisOperate.hgetAll(bean.redisKey(), bean);
	}

	public void flushHashBean(RedisHashBean bean) { 
		redisOperate.hmset(bean.redisKey(), BeanUtils.beanToMap(bean));
	}
}
