package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.realm.CommonService;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.service.realm.relation.RelationService;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.cache.OrderCache;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;

public interface Beans {
	
	final OrderCache orderCache = SpringContextUtil.getBean("orderCache", OrderCache.class);
	final JmsService jmsService = SpringContextUtil.getBean("jmsService", JmsService.class);
	final RedisOperate redisOperate = SpringContextUtil.getBean("redisOperate", RedisOperate.class);
	final AliyunService aliyunService = SpringContextUtil.getBean("aliyunService", AliyunService.class); 
	final RelationService relationService = SpringContextUtil.getBean("relationService", RelationService.class);
	
	final JiLuLuaOperate luaOperate = SpringContextUtil.getBean("luaOperate", JiLuLuaOperate.class); 
	final CommonService commonService = SpringContextUtil.getBean("commonService", CommonService.class);
	final DistributeLock distributeLock = SpringContextUtil.getBean("distributeLock", DistributeLock.class);
	final CourierService courierService = SpringContextUtil.getBean("courierService", CourierService.class);
	final MerchantService merchantService = SpringContextUtil.getBean("merchantService", MerchantService.class);
	
	final MemOrderMapper memOrderMapper = SpringContextUtil.getBean("memOrderMapper", MemOrderMapper.class);
	final MemMerchantMapper memMerchantMapper = SpringContextUtil.getBean("memMerchantMapper", MemMerchantMapper.class);
	final MemCustomerMapper memCustomerMapper = SpringContextUtil.getBean("memCustomerMapper", MemCustomerMapper.class);
	final MemOrderGoodsMapper memOrderGoodsMapper = SpringContextUtil.getBean("memOrderGoodsMapper", MemOrderGoodsMapper.class);
	final CfgGoodsMapper cfgGoodsMapper = SpringContextUtil.getBean("cfgGoodsMapper", CfgGoodsMapper.class);
}
