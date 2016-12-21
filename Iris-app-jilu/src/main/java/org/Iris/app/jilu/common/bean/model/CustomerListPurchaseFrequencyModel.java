package org.Iris.app.jilu.common.bean.model;

public class CustomerListPurchaseFrequencyModel {

	private int customerId;
	private int count;
	
	public long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
}
