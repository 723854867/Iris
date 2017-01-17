package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.common.bean.enums.MerchantStatusMod;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class MerchantForm {

	private long merchantId;
	private String token;
	private String avatar;
	private String name;
	private String address;
	private int statusMod;
	
	public MerchantForm(Merchant merchant) {
		MemMerchant memMerchant = merchant.getMemMerchant();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
		this.statusMod = memMerchant.getStatusMod();
		this.name = memMerchant.getName();
		this.address = memMerchant.getAddress();
		
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
	
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getStatusMod() {
		return statusMod;
	}
}
