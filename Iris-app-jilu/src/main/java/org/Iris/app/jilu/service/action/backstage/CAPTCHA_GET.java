package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;

/**
 * 获取验证码(手机和邮箱获取验证码的规则是一样的)
 * 
 * @author ahab
 */
public class CAPTCHA_GET extends BackstageAction {
	
	@Override
	protected String execute0(IrisSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);
		Merchant merchant = merchantService.getMerchantByAccount(type, account);
		if(merchant == null)
			return Result.jsonError(JiLuCode.ACOUNT_IS_NOT_EXIST);
		return merchantWebService.generateCaptcha(type, account);
	}
}