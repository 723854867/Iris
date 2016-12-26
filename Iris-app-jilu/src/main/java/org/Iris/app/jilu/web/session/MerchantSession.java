package org.Iris.app.jilu.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.core.exception.IrisRuntimeException;

public class MerchantSession extends IrisSession {
	
	private Merchant merchant;
	
	public MerchantSession(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public Merchant getMerchant() {
		return merchant;
	}
	
	public void bind(Merchant merchant) { 
		if (null != this.merchant) 
			throw new IrisRuntimeException("MerchantSession already bind with a merchant!");
		this.merchant = merchant;
	}
}
