package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemOrder implements RedisHashBean{

	private String orderId;
	private String rootOrderId;
	private String superOrderId;
	private long superMerchantId;
	private String superMerchantName;
	private long merchantId;
	private String merchantName;
	private String merchantAddress;
	private long customerId;
	private String customerName;
	private String customerMobile;
	private String customerAddress;
	private String memo;
	private String shMemo;//售后完成的备注
	private String shInfo;//售后的要求说明
	private int shTime;//售后的预计处理时间
	private int status;
	private int created;
	private int updated;
	private int deleted;

	public MemOrder() {
	}

	public MemOrder(long merchantId,String orderId) {
		this.merchantId = merchantId;
		this.orderId = orderId;
	}

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

	public long getSuperMerchantId() {
		return superMerchantId;
	}

	public void setSuperMerchantId(long superMerchantId) {
		this.superMerchantId = superMerchantId;
	}

	public String getSuperMerchantName() {
		return superMerchantName;
	}

	public void setSuperMerchantName(String superMerchantName) {
		this.superMerchantName = superMerchantName;
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
	
	public String getMerchantAddress() {
		return merchantAddress;
	}

	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
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

	public String getShMemo() {
		return shMemo;
	}

	public void setShMemo(String shMemo) {
		this.shMemo = shMemo;
	}

	public String getShInfo() {
		return shInfo;
	}

	public void setShInfo(String shInfo) {
		this.shInfo = shInfo;
	}

	public int getShTime() {
		return shTime;
	}

	public void setShTime(int shTime) {
		this.shTime = shTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantOrderDataKey(merchantId, orderId);
	}
}
