package org.Iris.app.jilu.storage.domain;

import org.Iris.redis.RedisHashBean;

public class OrderGoods implements RedisHashBean {

	private String orderId;
	private int goodsId;
	private int goodsCount;
	private float goodsPrice;

	public OrderGoods() {
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	@Override
	public String redisKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
