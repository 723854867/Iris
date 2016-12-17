package org.Iris.app.jilu.web.auth;

import javax.annotation.Resource;

import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.redis.cache.UnitCache;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.MerchantSession;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.springframework.stereotype.Component;

@Component
public class MerchantAuthenticator implements Authenticator<MerchantSession> {
	
	@Resource
	private UnitCache unitCache;

	@Override
	public boolean auth(MerchantSession session) {
		String token = session.getHeader(JiLuParams.TOKEN);
		Merchant merchant = unitCache.getMerchantByToken(token);
		if (null == merchant) {
			session.write(Result.jsonError(ICode.Code.TOKEN_INVALID));
			return false;
		}
		session.setUnit(merchant);
		return true;
	}
}
