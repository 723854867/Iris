package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;

public class OrderGoodsForm {

	private long id;
	private long goodsId;
	private String orderId;
	private String goodsName;
	private int count;
	private String unitPrice;
	
	public OrderGoodsForm(MemOrderGoods memOrderGoods){
		this.id = memOrderGoods.getId();
		this.goodsId = memOrderGoods.getGoodsId();
		this.orderId = memOrderGoods.getOrderId();
		this.goodsName = memOrderGoods.getGoodsName();
		this.count = memOrderGoods.getCount();
		this.unitPrice = memOrderGoods.getUnitPrice();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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
}
