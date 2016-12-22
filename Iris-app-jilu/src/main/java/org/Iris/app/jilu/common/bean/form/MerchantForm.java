package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.service.realm.unit.merchant.MerchantOperator;
import org.Iris.app.jilu.storage.domain.Merchant;

public class MerchantForm {

	private long merchantId;
	private String token;
	private String avatar;
	
	public MerchantForm(MerchantOperator merchant) {
		Merchant memMerchant = merchant.getUnit();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
		this.avatar = JiLuResourceUtil.merchantAvatarUri(memMerchant);
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
