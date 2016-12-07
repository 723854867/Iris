package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.jms.JmsService;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;
import org.Iris.redis.operate.lua.LuaOperate;

public interface Beans {
	
	final JmsService jmsService = SpringContextUtil.getBean("jmsService", JmsService.class);
	final LuaOperate luaOperate = SpringContextUtil.getBean("luaOperate", LuaOperate.class); 
	final RedisOperate redisOperate = SpringContextUtil.getBean("redisOperate", RedisOperate.class);
	final DistributeLock distributeLock = SpringContextUtil.getBean("distributeLock", DistributeLock.class);
}
