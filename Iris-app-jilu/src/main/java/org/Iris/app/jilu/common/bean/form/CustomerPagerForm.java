package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.MemCustomer;

public class CustomerPagerForm {

	private long customerId;
	private String name;
	private String mobile;
	private String purchaseSum;
	private int lastPurchaseTime;
	
	public CustomerPagerForm(MemCustomer customer) {
		this.customerId = customer.getCustomerId();
		this.name = customer.getName();
		this.mobile = customer.getMobile();
		this.purchaseSum = customer.getPurchaseSum();
		this.lastPurchaseTime = customer.getLastPurchaseTime();
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
