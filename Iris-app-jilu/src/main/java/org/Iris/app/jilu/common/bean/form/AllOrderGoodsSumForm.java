package org.Iris.app.jilu.common.bean.form;
/**
 * 根据时间段查询所有订单待采购清单总和 表单
 * @author Administrator
 *
 */
public class AllOrderGoodsSumForm {

	private long goodsId;
	private String goodsName;
	private String merchantName;
	private float unitPrice;
	private long count;
	public AllOrderGoodsSumForm() {
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	
}
