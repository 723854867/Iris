package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.relation.RelationService;
import org.Iris.app.jilu.storage.redis.cache.OrderCache;
import org.Iris.app.jilu.storage.redis.cache.UnitCache;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;
import org.Iris.redis.operate.lua.LuaOperate;

public interface Beans {
	
	final UnitCache unitCache = SpringContextUtil.getBean("unitCache", UnitCache.class);
	final OrderCache orderCache = SpringContextUtil.getBean("orderCache", OrderCache.class);
	final JmsService jmsService = SpringContextUtil.getBean("jmsService", JmsService.class);
	final LuaOperate luaOperate = SpringContextUtil.getBean("luaOperate", LuaOperate.class); 
	final RedisOperate redisOperate = SpringContextUtil.getBean("redisOperate", RedisOperate.class);
	final AliyunService aliyunService = SpringContextUtil.getBean("aliyunService", AliyunService.class); 
	final DistributeLock distributeLock = SpringContextUtil.getBean("distributeLock", DistributeLock.class);
	final RelationService relationService = SpringContextUtil.getBean("relationService", RelationService.class);
}
