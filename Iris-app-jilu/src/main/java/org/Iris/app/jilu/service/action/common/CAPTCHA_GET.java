package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.KeyUtil;

/**
 * 获取验证码(手机和邮箱获取验证码的规则是一样的)
 * 
 * @author ahab
 */
public class CAPTCHA_GET extends CommonAction {
	
	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);
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
			return Result.jsonSuccess(captcha);				
		case ONLINE:										// 线上环境需要发送短信
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess();					
		default:
			return Result.jsonError(ICode.Code.SYSTEM_ERROR);
		}
	}
}
