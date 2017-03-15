package org.Iris.app.jilu.storage.domain;

import org.Iris.app.jilu.storage.redis.MerchantKeyGenerator;
import org.Iris.redis.RedisHashBean;

public class MemOrderGoods implements RedisHashBean{

	private long id;
	private long changeId;//该字段不存在于数据表，只是临时用于接收转单时传递的参数所用；
	private String orderId;
	private String changeOrderId="0";//如果产品状态为转单成功时显示转单的订单号
	private String packetId="0";
	private long goodsId;
	private String goodsName;
	private int goodsImage;
	private int count;
	private String unitPrice;
	private int status;
	private int created;
	private int updated;

	public MemOrderGoods() {
	}
	public MemOrderGoods(String orderId,long goodsId) {
		this.orderId = orderId;
		this.goodsId = goodsId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getChangeId() {
		return changeId;
	}
	public void setChangeId(long changeId) {
		this.changeId = changeId;
	}
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChangeOrderId() {
		return changeOrderId;
	}
	public void setChangeOrderId(String changeOrderId) {
		this.changeOrderId = changeOrderId;
	}
	public String getPacketId() {
		return packetId;
	}

	public void setPacketId(String packetId) {
		this.packetId = packetId;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(int goodsImage) {
		this.goodsImage = goodsImage;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
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
		//return MerchantKeyGenerator.merchantOrderGoodsDataKey(orderId, goodsId);
		return MerchantKeyGenerator.merchantOrderGoodsDataKey(id);
	}

}
