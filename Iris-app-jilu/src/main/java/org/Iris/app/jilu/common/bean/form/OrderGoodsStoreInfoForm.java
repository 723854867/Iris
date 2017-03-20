package org.Iris.app.jilu.common.bean.form;
/**
 * 订单产品库存对比
 * @author 樊水东
 * 2017年3月3日
 */
public class OrderGoodsStoreInfoForm {

	private long goodsId;
	private String goodsName;
	//需求数量
	private long needCount;
	//库存中是否创建
	private int isCreated;
	//库存数量
	private long storeCount; 
	//采购成本
	private float price;
	
	public OrderGoodsStoreInfoForm() {
		// TODO Auto-generated constructor stub
	}
	public OrderGoodsStoreInfoForm(long goodsId, String goodsName, long needCount,int isCreated, long storeCount, float price) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.needCount = needCount;
		this.isCreated = isCreated;
		this.storeCount = storeCount;
		this.price = price;
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
	public long getNeedCount() {
		return needCount;
	}
	public void setNeedCount(long needCount) {
		this.needCount = needCount;
	}
	public int getIsCreated() {
		return isCreated;
	}
	public void setIsCreated(int isCreated) {
		this.isCreated = isCreated;
	}
	public long getStoreCount() {
		return storeCount;
	}
	public void setStoreCount(long storeCount) {
		this.storeCount = storeCount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
	
	
}
