package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.storage.domain.MemCustomer;

public class CustomerForm {

	private long customerId;
	private String name;
	private String mobile;
	private String address;
	private String memo;
	private String IDFrontage;
	private String IDBehind;
	private String purchaseSum;
	private int lastPurchaseTime;
	
	public CustomerForm(MemCustomer customer) {
		this.customerId = customer.getCustomerId();
		this.name = customer.getName();
		this.mobile = customer.getMobile();
		this.address = customer.getAddress();
		this.memo = customer.getMemo();
		this.purchaseSum = customer.getPurchaseSum();
		this.lastPurchaseTime = customer.getLastPurchaseTime();
		this.IDFrontage = JiLuResourceUtil.customerIDFrontageUri(customer);
		this.IDBehind = JiLuResourceUtil.customerIDBehindUri(customer);
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIDFrontage() {
		return IDFrontage;
	}

	public void setIDFrontage(String iDFrontage) {
		IDFrontage = iDFrontage;
	}

	public String getIDBehind() {
		return IDBehind;
	}

	public void setIDBehind(String iDBehind) {
		IDBehind = iDBehind;
	}

	public String getPurchaseSum() {
		return purchaseSum;
	}

	public void setPurchaseSum(String purchaseSum) {
		this.purchaseSum = purchaseSum;
	}

	public int getLastPurchaseTime() {
		return lastPurchaseTime;
	}

	public void setLastPurchaseTime(int lastPurchaseTime) {
		this.lastPurchaseTime = lastPurchaseTime;
	}
}
