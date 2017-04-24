package org.Iris.app.jilu.common;

import org.Iris.app.jilu.service.realm.BackstageService;
import org.Iris.app.jilu.service.realm.CommonService;
import org.Iris.app.jilu.service.realm.aliyun.AliyunService;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.igt.IgtService;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.service.realm.pay.PayService;
import org.Iris.app.jilu.service.realm.relation.RelationService;
import org.Iris.app.jilu.service.realm.weixin.WeiXinService;
import org.Iris.app.jilu.service.realm.wyyx.SmsService;
import org.Iris.app.jilu.service.realm.wyyx.WyyxService;
import org.Iris.app.jilu.storage.mybatis.mapper.BgConfigMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.BgUserMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.BgVersionMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.CfgGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccidMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemAccountMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCidMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemCustomerMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemGoodsStoreMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemMerchantMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderGoodsMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderPacketMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemOrderStatusMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemPayInfoMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.MemWaitStoreMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.StockGoodsStoreLogMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.SysMenuMapper;
import org.Iris.app.jilu.storage.mybatis.mapper.UpdateStoreLogMapper;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.core.util.SpringContextUtil;
import org.Iris.redis.operate.RedisOperate;
import org.Iris.redis.operate.lock.DistributeLock;
import org.Iris.util.network.http.HttpProxy;

public interface Beans {
	
	final RedisOperate redisOperate = SpringContextUtil.getBean("redisOperate", RedisOperate.class);
	final AliyunService aliyunService = SpringContextUtil.getBean("aliyunService", AliyunService.class); 
	final RelationService relationService = SpringContextUtil.getBean("relationService", RelationService.class);
	
	final JiLuLuaOperate luaOperate = SpringContextUtil.getBean("luaOperate", JiLuLuaOperate.class); 
	final CommonService commonService = SpringContextUtil.getBean("commonService", CommonService.class);
	final BackstageService backstageService = SpringContextUtil.getBean("backstageService", BackstageService.class);
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
	final MemAccidMapper memAccidMapper = SpringContextUtil.getBean("memAccidMapper", MemAccidMapper.class);
	final StockGoodsStoreLogMapper stockGoodsStoreLogMapper = SpringContextUtil.getBean("stockGoodsStoreLogMapper", StockGoodsStoreLogMapper.class);
	final MemWaitStoreMapper memWaitStoreMapper = SpringContextUtil.getBean("memWaitStoreMapper", MemWaitStoreMapper.class);
	final UpdateStoreLogMapper updateStoreLogMapper = SpringContextUtil.getBean("updateStoreLogMapper", UpdateStoreLogMapper.class);
	final HttpProxy httpProxy = SpringContextUtil.getBean("httpProxy", HttpProxy.class);
	final MemPayInfoMapper memPayInfoMapper = SpringContextUtil.getBean("memPayInfoMapper", MemPayInfoMapper.class);
	
	final IgtService igtService = SpringContextUtil.getBean("igtService", IgtService.class);
	final WyyxService wyyxService = SpringContextUtil.getBean("wyyxService", WyyxService.class);
	final WeiXinService weiXinService = SpringContextUtil.getBean("weiXinService", WeiXinService.class);
	final PayService payService = SpringContextUtil.getBean("payService", PayService.class);
	final SmsService smsService = SpringContextUtil.getBean("smsService", SmsService.class);
	
	final BgUserMapper bgUserMapper = SpringContextUtil.getBean("bgUserMapper", BgUserMapper.class);
	final SysMenuMapper sysMenuMapper = SpringContextUtil.getBean("sysMenuMapper", SysMenuMapper.class);
	final BgConfigMapper bgConfigMapper = SpringContextUtil.getBean("bgConfigMapper", BgConfigMapper.class);
	final BgVersionMapper bgVersionMapper = SpringContextUtil.getBean("bgVersionMapper", BgVersionMapper.class);
	
	
}
