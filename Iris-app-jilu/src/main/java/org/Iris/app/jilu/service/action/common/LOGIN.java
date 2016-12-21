package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.common.SerializeUtil;

/**
 * 商户登陆
 * 
 * @author Ahab
 */
public class LOGIN extends CommonAction {
	
	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);

		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!luaOperate.delIfEquals(CommonKeyGenerator.getAccountCaptchaKey(type, account), session.getKVParam(JiLuParams.CAPTCHA)))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			
			MerchantOperator operator = unitCache.getMerchantByAccount(type, account);
			if (null == operator) {
				String token = IrisSecurity.encodeToken(account);
				String key = CommonKeyGenerator.createMarkDataKey(token);
				redisOperate.hmset(key, new AccountModel(type, account));
				redisOperate.expire(key, AppConfig.CREATE_WAIT_TIMEOUT);
				Result<String> result = new Result<String>(ICode.Code.UNIT_NOT_EXIST);
				result.setData(token);
				return SerializeUtil.JsonUtil.GSON.toJson(result);
			}
			
			if (!operator.login(account, false))
				return Result.jsonError(ICode.Code.REQUEST_FREQUENTLY);
			return Result.jsonSuccess(new MerchantForm(operator));
		case WECHAT:
			return Result.jsonSuccess();
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}
}
