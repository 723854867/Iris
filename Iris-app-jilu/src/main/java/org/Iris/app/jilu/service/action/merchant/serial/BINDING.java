package org.Iris.app.jilu.service.action.merchant.serial;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.service.action.merchant.SerialMerchantAction;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;

/**
 * 手机或邮箱绑定
 * @author 樊水东
 * 2017年1月19日
 */
public class BINDING extends SerialMerchantAction{

	@Override
	protected String execute0(MerchantSession session) {
		AccountType type = AccountType.match(session.getKVParamOptional(JiLuParams.TYPE));
		String account = type == AccountType.MOBILE ? session.getKVParam(JiLuParams.MOBILE) : session.getKVParam(JiLuParams.EMAIL);
		return merchantService.bindingPhoneOrMobile(account, type, session.getKVParam(JiLuParams.CAPTCHA), session.getMerchant().getMemMerchant().getMerchantId());
	}

	
}
