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
		String account=null;
		switch (type) {
		case MOBILE:
			account = session.getKVParam(JiLuParams.MOBILE);
			break;
		case EMAIL:
			account = session.getKVParam(JiLuParams.EMAIL);
			break;
		case WECHAT:
			account = session.getKVParam(JiLuParams.OPENID);
			break;
		default:
		}
		return merchantService.bindingPhoneOrMobile(account, type, session.getKVParam(JiLuParams.CAPTCHA), session.getMerchant().getMemMerchant().getMerchantId());
	}

	
}
