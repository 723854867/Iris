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
	private String phone;
	private String email;
	private String weixin;
	private String accid;
	private String accidToken;
	
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

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatusMod() {
		return statusMod;
	}

	public void setStatusMod(int statusMod) {
		this.statusMod = statusMod;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getAccidToken() {
		return accidToken;
	}

	public void setAccidToken(String accidToken) {
		this.accidToken = accidToken;
	}
	
	
}
