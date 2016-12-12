package org.Iris.app.jilu.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;

public class MerchantSession extends UnitSession<Merchant> {
	
	public MerchantSession(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
}
