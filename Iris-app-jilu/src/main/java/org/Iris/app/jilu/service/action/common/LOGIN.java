package org.Iris.app.jilu.service.action.common;

import org.Iris.app.jilu.common.bean.form.MerchantForm;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.CommonAction;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.redis.RedisKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;

/**
 * 登陆
 * 
 * @author Ahab
 */
public class LOGIN extends CommonAction {
	
	public static final LOGIN INSTANCE						= new LOGIN();
	
	private LOGIN() {}

	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);

		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!luaOperate.delIfEquals(RedisKeyGenerator.getAccountCaptchaKey(type, account), session.getKVParam(JiLuParams.CAPTCHA)))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			
			Merchant merchant = merchantService.getMerhantByAccount(account);
			if (null == merchant)
				return Result.jsonError(ICode.Code.UNIT_NOT_EXIST);
			
			merchant.login(account);
			return Result.jsonSuccess(new MerchantForm(merchant));
		case WECHAT:
			return Result.jsonSuccess();
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
	}
}
