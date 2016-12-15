package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.AliyunUtils;
import org.Iris.app.jilu.service.realm.unit.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class MerchantForm {

	private long merchantId;
	private String avatar;
	private String token;
	
	public MerchantForm(Merchant merchant) {
		MemMerchant memMerchant = merchant.getUnit();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
		this.avatar = AliyunUtils.getAvatar(memMerchant.getAvatar());
	}
	
	public long getMerchantId() {
		return merchantId;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getAvatar() {
		return avatar;
	}
}
