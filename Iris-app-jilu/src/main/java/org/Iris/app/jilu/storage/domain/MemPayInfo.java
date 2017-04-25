package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;
import org.Iris.util.lang.DateUtils;

/**
 * 商户充值信息
 * @author 樊水东
 * 2017年4月17日
 */
public class MemPayInfo implements RedisHashBean{

	private long id;
	private long merchantId;
	private String outTradeNo;
	private int payType;
	private String subject;
	private String body;
	private float totalAmount;
	private int status;
	private int created;
	private int updated;
	
	public MemPayInfo() {
	}

	public MemPayInfo(long merchantId, String outTradeNo, int payType, String subject, String body, float totalAmount) {
		int time = DateUtils.currentTime();
		this.merchantId = merchantId;
		this.outTradeNo = outTradeNo;
		this.payType = payType;
		this.subject = subject;
		this.body = body;
		this.totalAmount = totalAmount;
		this.created = time;
		this.updated = time;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
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

	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantPayDataKey();
	}
	
	
}