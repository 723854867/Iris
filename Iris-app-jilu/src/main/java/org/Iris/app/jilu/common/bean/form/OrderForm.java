package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.MemOrder;

public class OrderForm {

	private String orderId;
	private long merchantId;
	private String merchantName;
	private long customerId;
	private String customerName;
	private int created;
	private int updated;
	
	public OrderForm(MemOrder order){
		this.orderId = order.getOrderId();
		this.merchantId = order.getMerchantId();
		this.merchantName = order.getMerchantName();
		this.customerId = order.getCustomerId();
		this.customerName = order.getCustomerName();
		this.created = order.getCreated();
		this.updated = order.getUpdated();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public int getUpdated() {
		return updated;
	}

	public void setUpdated(int updated) {
		this.updated = updated;
	}
	
	
}
