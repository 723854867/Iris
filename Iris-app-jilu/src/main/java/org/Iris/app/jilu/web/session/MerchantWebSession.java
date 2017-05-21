package org.Iris.app.jilu.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.core.exception.IrisRuntimeException;

public class MerchantWebSession extends IrisSession {
	
	private MemMerchant memMerchant;
	
	public MerchantWebSession(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	
	public MemMerchant getMemMerchant() {
		return memMerchant;
	}

	public void bind(MemMerchant memMerchant) { 
		if (null != this.memMerchant) 
			throw new IrisRuntimeException("MerchantWebSession already bind with a memMerchant!");
		this.memMerchant = memMerchant;
	}
}
