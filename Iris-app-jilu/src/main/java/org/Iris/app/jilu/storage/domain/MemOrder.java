package org.Iris.app.jilu.storage.domain;

public class MemOrder {

	private String orderId;
	private String rootOrderId;
	private String superOrderId;
	private long merchantId;
	private String merchantName;
	private long customerId;
	private String customerName;
	private String customerMobile;
	private String customerAddress;
	private int status;
	private int created;
	private int updated;
	private int deleted;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRootOrderId() {
		return rootOrderId;
	}

	public void setRootOrderId(String rootOrderId) {
		this.rootOrderId = rootOrderId;
	}

	public String getSuperOrderId() {
		return superOrderId;
	}

	public void setSuperOrderId(String superOrderId) {
		this.superOrderId = superOrderId;
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

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
}
