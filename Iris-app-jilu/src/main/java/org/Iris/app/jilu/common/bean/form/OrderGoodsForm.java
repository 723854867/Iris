package org.Iris.app.jilu.common.bean.form;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;

public class OrderGoodsForm {

	private long id;
	private long goodsId;
	private String orderId;
	private String goodsName;
	private long count;
	private String unitPrice;
	
	public OrderGoodsForm(MemOrderGoods memOrderGoods){
		this.id = memOrderGoods.getId();
		this.goodsId = memOrderGoods.getGoodsId();
		this.orderId = memOrderGoods.getOrderId();
		this.goodsName = memOrderGoods.getGoodsName();
		this.count = memOrderGoods.getCount();
		this.unitPrice = memOrderGoods.getUnitPrice();
	}
	
	public static List<OrderGoodsForm> getList(List<MemOrderGoods> list){
		List<OrderGoodsForm> forms = new ArrayList<OrderGoodsForm>();
		for(MemOrderGoods memOrderGoods : list)
			forms.add(new OrderGoodsForm(memOrderGoods));
		return forms;
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
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
}
