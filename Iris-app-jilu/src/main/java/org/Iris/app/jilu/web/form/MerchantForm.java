package org.Iris.app.jilu.web.form;

import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class MerchantForm {

	private long merchantId;
	private String token;
	
	public MerchantForm(Merchant merchant) {
		MemMerchant memMerchant = merchant.getUnit();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
	}
	
	public long getMerchantId() {
		return merchantId;
	}
	
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
