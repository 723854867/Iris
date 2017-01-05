package org.Iris.app.jilu.service.realm;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.realm.courier.CourierService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.JiLuLuaOperate;
import org.Iris.app.jilu.storage.redis.RedisCache;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.common.SerializeUtil;
import org.springframework.stereotype.Service;

@Service
public class CommonService extends RedisCache {
	
	@Resource
	private JiLuLuaOperate luaOperate;
	@Resource
	private CourierService courierService;
	@Resource
	private MerchantService merchantService;

	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 */
	public String login(AccountType type, String account, String captch) { 
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			
			Merchant merchant = merchantService.getMerchantByAccount(type, account);
			if (null == merchant) {
				String token = IrisSecurity.encodeToken(account);
				String key = CommonKeyGenerator.createMarkDataKey(token);
				redisOperate.hmset(key, new AccountModel(type, account));
				redisOperate.expire(key, AppConfig.CREATE_WAIT_TIMEOUT);
				Result<String> result = new Result<String>(ICode.Code.UNIT_NOT_EXIST);
				result.setData(token);
				return SerializeUtil.JsonUtil.GSON.toJson(result);
			}
			
			if (!merchant.login(account, false))
				return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
			return Result.jsonSuccess(new MerchantForm(merchant));
		case WECHAT:
			return Result.jsonSuccess();
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}
	
	/**
	 * 创建用户
	 * 
	 * @param token
	 * @param name
	 * @param address
	 * @return
	 */
	public String create(String token, String name, String address) {
		AccountModel am = luaOperate.delAndGetHash(CommonKeyGenerator.createMarkDataKey(token), new AccountModel());
		if (null == am)
			throw IllegalConstException.errorException(JiLuParams.TOKEN);
		Merchant merchant = merchantService.createMerchant(am, name, address);
		if (!merchant.login(am.getAccount(), true))
			return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
		return Result.jsonSuccess(new MerchantForm(merchant));
	}
}
