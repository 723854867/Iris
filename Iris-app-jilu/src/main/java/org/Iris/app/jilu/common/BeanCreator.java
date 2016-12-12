package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.util.lang.DateUtils;

public class BeanCreator {

	public static MemMerchant newMemMerchant(String name, String avatar, String address) { 
		MemMerchant memMerchant = new MemMerchant();
		memMerchant.setAddress(address);
		memMerchant.setAvatar(avatar);
		memMerchant.setName(name);
		int time = DateUtils.currentTime();
		memMerchant.setCreated(time);
		memMerchant.setUpdated(time);
		memMerchant.setLastLoginTime(time);
		return memMerchant;
	}
	
	public static MemAccount newMemAccount(AccountModel am, int time, long merchantId) {
		MemAccount memAccount = new MemAccount();
		memAccount.setMerchantId(merchantId);
		memAccount.setAccount(am.getAccount());
		memAccount.setType(am.getType());
		memAccount.setUpdated(time);
		memAccount.setCreated(time);
		return memAccount;
	}
}
