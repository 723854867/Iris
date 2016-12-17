package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.bean.model.AccountModel;
import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.util.lang.DateUtils;

/**
 * 转么负责domain对象创建
 * 
 * @author ahab
 */
public class BeanCreator {

	public static MemMerchant newMemMerchant(String name, String address) { 
		MemMerchant memMerchant = new MemMerchant();
		memMerchant.setAddress(address);
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
	
	public static MemCustomer newMemCustomer(long merchantId, String name, String mobile, String address, String memo, String IDNumber) { 
		MemCustomer memCustomer = new MemCustomer();
		memCustomer.setMerchantId(merchantId);
		memCustomer.setName(name);
		memCustomer.setMobile(mobile);
		memCustomer.setAddress(address);
		memCustomer.setMemo(memo);
		memCustomer.setIDNumber(IDNumber);
		int time = DateUtils.currentTime();
		memCustomer.setCreated(time);
		memCustomer.setUpdated(time);
		return memCustomer;
	}
}
