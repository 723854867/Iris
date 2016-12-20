package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.MemCustomer;

public class CustomerFrequencyPagerForm extends CustomerPagerForm {
	
	private int purchaseCount;

	public CustomerFrequencyPagerForm(MemCustomer customer) {
		super(customer);
	}

	public int getPurchaseCount() {
		return purchaseCount;
	}
	
	public void setPurchaseCount(int purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
}
