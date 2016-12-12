package org.Iris.app.jilu.web.auth;

import org.Iris.app.jilu.web.session.IrisSession;
import org.springframework.stereotype.Component;

@Component
public class MerchantAuthenticator implements Authenticator {

	@Override
	public boolean auth(IrisSession session) {
			
		return false;
	}
}
