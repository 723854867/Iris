package org.Iris.app.jilu.common.bean.form;

import java.util.UUID;

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
	private int money;
	private int statusMod;
	private String phone;
	private String email;
	private String sendName;
	private String sendAddress;
	private String sendMobile;
	private String openId;
	private String accessToken;
	private String accid;
	private String accidToken;
	
	public MerchantForm(Merchant merchant) {
		MemMerchant memMerchant = merchant.getMemMerchant();
		this.merchantId = memMerchant.getMerchantId();
		this.token = memMerchant.getToken();
		this.statusMod = memMerchant.getStatusMod();
		this.name = memMerchant.getName();
		this.address = memMerchant.getAddress();
		this.sendName = memMerchant.getSendName();
		this.sendAddress = memMerchant.getSendAddress();
		this.sendMobile = memMerchant.getSendMobile();
		this.money = memMerchant.getMoney();
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

	public String getSendName() {
		return sendName;
	}

	public void setSendName(String sendName) {
		this.sendName = sendName;
	}

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public String getSendMobile() {
		return sendMobile;
	}

	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
