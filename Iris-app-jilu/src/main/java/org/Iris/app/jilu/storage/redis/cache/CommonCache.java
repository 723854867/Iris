package org.Iris.app.jilu.storage.redis.cache;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.redis.operate.lua.LuaOperate;
import org.Iris.util.common.IrisSecurity;
import org.springframework.stereotype.Component;

@Component
public class CommonCache extends RedisCache {
	
	@Resource
	private LuaOperate luaOperate;

	/**
	 * 登陆失败之后需要对失败的账号做一个暂时的标记，该账号三分钟之类可以去创建账号，如果不创建则需要重新获取验证码才可以登陆
	 * 
	 * @param type
	 * @param account
	 * @param token
	 */
	public String markAccountData(AccountType type, String account) { 
		String token = IrisSecurity.encodeToken(account);
		String key = CommonKeyGenerator.createMarkDataKey(token);
		redisOperate.hmset(key, new AccountModel(type, account));
		redisOperate.expire(key, AppConfig.CREATE_WAIT_TIMEOUT);
		return token;
	}
	
	/**
	 * 获取登陆失败之后为创建用户而保存的数据，时间是三分钟有效期
	 * 
	 * @param token
	 * @return
	 */
	public AccountModel getMarkAccountData(String token) { 
		return luaOperate.delAndGetHash(CommonKeyGenerator.createMarkDataKey(token), new AccountModel());
	}
}
