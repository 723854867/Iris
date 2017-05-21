package org.Iris.app.jilu.service.realm.courier;

import javax.annotation.Resource;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.wyyx.result.SendSmsResult;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.redis.operate.lua.LuaOperate;
import org.Iris.util.common.KeyUtil;
import org.springframework.stereotype.Service;

@Service
public class CourierService {
	
	@Resource
	private LuaOperate luaOperate;
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
		String captchaKey = CommonKeyGenerator.accountCaptchaKey(type, account);
		String captchaCountKey = CommonKeyGenerator.accountCaptchaCountKey(type, account);
		
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
//			SendSmsResult result = Beans.smsService.sendSms(account.substring(3), 3060303, 4);
//			if(result.getCode() == 200)
//				result.setCode(0);
			//jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess(captcha);				
		case ONLINE:										// 线上环境需要发送短信
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess();					
		default:
			return Result.jsonError(ICode.Code.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 检查验证码
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public boolean verifyCaptch(AccountType type, String account, String captch) {
		return luaOperate.delIfEquals(CommonKeyGenerator.accountCaptchaKey(type, account), captch);
//		switch (type) {
//		case MOBILE:
//			SendSmsResult result = Beans.smsService.validateSms(account, captch);
//			return result.getCode()==200;
//		case EMAIL:
//			return luaOperate.delIfEquals(CommonKeyGenerator.accountCaptchaKey(type, account), captch);
//		default:
//			return false;
//		}
	}
}
