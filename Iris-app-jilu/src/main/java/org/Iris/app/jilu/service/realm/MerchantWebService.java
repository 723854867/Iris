package org.Iris.app.jilu.service.realm;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.redis.WebKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.KeyUtil;
import org.springframework.stereotype.Service;

@Service
public class MerchantWebService implements Beans{
	
	@Resource
	private JmsService jmsService;

	/**
	 * 生成验证码: 验证码和 账号对应
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public String generateCaptcha(AccountType type, String account) {
		String captchaKey = WebKeyGenerator.accountCaptchaKey(type, account);
		String captchaCountKey = WebKeyGenerator.accountCaptchaCountKey(type, account);
		
		// 生成验证码并且缓存验证码
		String captcha = KeyUtil.randomCaptcha(AppConfig.getCaptchaDigit());
		long flag = luaOperate.recordCaptcha(captchaKey, captchaCountKey, captcha, 
				AppConfig.getCaptchaLifeTime(), AppConfig.getCaptchaCountMaximum(), AppConfig.getCaptchaCountLifeTime());
		if (-1 == flag) 
			return Result.jsonError(JiLuCode.CAPTCHA_GET_CD);
		if (-2 == flag)
			return Result.jsonError(JiLuCode.CAPTCHA_COUNT_LIMIT);
		Env env = AppConfig.getEnv();
		switch (env) {
		case LOCAL:											// 测试环境下直接返回验证码
		case TEST:
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess(captcha);				
		case ONLINE:										// 线上环境需要发送短信
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess();					
		default:
			return Result.jsonError(ICode.Code.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 * @throws Exception 
	 */
	public String login(AccountType type, String account, String captch){ 
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!courierService.verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		
		Merchant merchant = merchantService.getMerchantByAccount(type, account);
		return Result.jsonSuccess(merchant.getMemMerchant());
		
	}
}
