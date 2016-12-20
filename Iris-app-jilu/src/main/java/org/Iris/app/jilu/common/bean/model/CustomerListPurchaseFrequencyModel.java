package org.Iris.app.jilu.common.bean.model;

import org.Iris.app.jilu.common.bean.enums.CustomerListType;

public class CustomerListPurchaseFrequencyModel implements CustomerListModel {

	private int customerId;
	private int count;
	
	@Override
	public long getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	@Override
	public double getScore(CustomerListType type) {
		return Double.valueOf(count);
	}
}
