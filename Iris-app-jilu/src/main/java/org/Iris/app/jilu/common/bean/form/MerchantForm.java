package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.common.bean.enums.MerchantStatusMod;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class MerchantForm {

	private long merchantId;
	private String token;
	private String avatar;
	private int statusMod;
	
	public MerchantForm(Merchant merchant) {
		MemMerchant memMerchant = merchant.getMemMerchant();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
		this.statusMod = memMerchant.getStatusMod();
		if (MerchantStatusMod.isQualified(memMerchant))
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
	
	public int getStatusMod() {
		return statusMod;
	}
}
