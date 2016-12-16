package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.realm.Tx;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.storage.redis.cache.MerchantCache;
import org.Iris.app.jilu.storage.redis.cache.OrderCache;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;
import org.Iris.redis.operate.lua.LuaOperate;

public interface Beans {
	
	final Tx tx = SpringContextUtil.getBean("tx", Tx.class);
	final OrderCache orderCache = SpringContextUtil.getBean("orderCache", OrderCache.class);
	final JmsService jmsService = SpringContextUtil.getBean("jmsService", JmsService.class);
	final LuaOperate luaOperate = SpringContextUtil.getBean("luaOperate", LuaOperate.class); 
	final RedisOperate redisOperate = SpringContextUtil.getBean("redisOperate", RedisOperate.class);
	final MerchantCache merchantCache = SpringContextUtil.getBean("merchantCache", MerchantCache.class);
	final AliyunService aliyunService = SpringContextUtil.getBean("aliyunService", AliyunService.class); 
	final DistributeLock distributeLock = SpringContextUtil.getBean("distributeLock", DistributeLock.class);
}
