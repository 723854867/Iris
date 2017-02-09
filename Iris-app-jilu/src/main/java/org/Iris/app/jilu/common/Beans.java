package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.realm.CommonService;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.igt.IgtService;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.service.realm.relation.RelationService;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCidMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsStoreMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderPacketMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderStatusMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;

public interface Beans {
	
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
	final MemOrderPacketMapper memOrderPacketMapper = SpringContextUtil.getBean("memOrderPacketMapper", MemOrderPacketMapper.class);
	final CfgGoodsMapper cfgGoodsMapper = SpringContextUtil.getBean("cfgGoodsMapper", CfgGoodsMapper.class);
	final MemGoodsStoreMapper memGoodsStoreMapper = SpringContextUtil.getBean("memGoodsStoreMapper", MemGoodsStoreMapper.class);
	final MemOrderStatusMapper memOrderStatusMapper = SpringContextUtil.getBean("memOrderStatusMapper", MemOrderStatusMapper.class);
	final MemAccountMapper memAccountMapper = SpringContextUtil.getBean("memAccountMapper", MemAccountMapper.class);
	final MemCidMapper memCidMapper = SpringContextUtil.getBean("memCidMapper", MemCidMapper.class);
	
	final IgtService igtService = SpringContextUtil.getBean("igtService", IgtService.class);
}
