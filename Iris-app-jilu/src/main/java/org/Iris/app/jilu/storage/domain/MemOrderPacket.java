package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;

/**
 * 邮包
 * @author 樊水东
 * 2017年1月3日
 */
public class MemOrderPacket implements RedisHashBean{

	private String packetId;
	private String orderId;
	private long merchantId;
	private String express;
	private String memo;
	private String expressCode;
	private String postage;
	private String label;
	private String latitude;
	private String longitude;
	private String status;
	private int created;
	private int updated;
	
	
	public MemOrderPacket() {
		
	}
	public String getPacketId() {
		return packetId;
	}
	public void setPacketId(String packetId) {
		this.packetId = packetId;
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
	public String getExpressCode() {
		return expressCode;
	}
	public void setExpressCode(String expressCode) {
		this.expressCode = expressCode;
	}
	public String getExpress() {
		return express;
	}
	public void setExpress(String express) {
		this.express = express;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
		return MerchantKeyGenerator.merchantOrderPacketDataKey(merchantId, packetId);
	}
	
}
