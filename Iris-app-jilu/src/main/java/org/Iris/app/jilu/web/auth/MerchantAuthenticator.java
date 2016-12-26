package org.Iris.app.jilu.web.auth;

import javax.annotation.Resource;

import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.service.realm.merchant.MerchantService;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.springframework.stereotype.Component;

@Component
public class MerchantAuthenticator implements Authenticator<MerchantSession> {
	
	@Resource
	private MerchantService merchantService;

	@Override
	public boolean auth(MerchantSession session) {
		String token = session.getHeader(JiLuParams.TOKEN);
		Merchant merchant = merchantService.getMerchantByToken(token);
		if (null == merchant) {
			session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
			return false;
		}
		session.bind(merchant);
		return true;
	}
}
