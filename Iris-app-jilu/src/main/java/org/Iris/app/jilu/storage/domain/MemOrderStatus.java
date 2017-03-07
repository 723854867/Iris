package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemOrderStatus implements RedisHashBean{

	private String orderId;
	private int transformCount;
	private int packetCount;
	private int transportCount;
	private int finishedCount;
	
	public MemOrderStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MemOrderStatus(String orderId) {
		this.orderId = orderId;
	}
	
	public MemOrderStatus(String orderId, int transformCount,
			int packetCount, int transportCount, int finishedCount) {
		super();
		this.orderId = orderId;
		this.transformCount = transformCount;
		this.packetCount = packetCount;
		this.transportCount = transportCount;
		this.finishedCount = finishedCount;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getTransformCount() {
		return transformCount;
	}
	public void setTransformCount(int transformCount) {
		this.transformCount = transformCount;
	}
	public int getPacketCount() {
		return packetCount;
	}
	public void setPacketCount(int packetCount) {
		this.packetCount = packetCount;
	}
	public int getTransportCount() {
		return transportCount;
	}
	public void setTransportCount(int transportCount) {
		this.transportCount = transportCount;
	}
	public int getFinishedCount() {
		return finishedCount;
	}
	public void setFinishedCount(int finishedCount) {
		this.finishedCount = finishedCount;
	}
	@Override
	public String redisKey() {
		return MerchantKeyGenerator.merchantOrderStatusDataKey(orderId);
	}
	
	
}
