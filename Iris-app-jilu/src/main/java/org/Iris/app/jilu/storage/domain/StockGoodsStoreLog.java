package org.Iris.app.jilu.storage.domain;
/**
 * 进货日志
 * @author fansd
 *
 */
public class StockGoodsStoreLog {
	private long goodsId;
	private String goodsName;
	private long merchantId;
	private String merchantName;
	private String memo;
	private float price;
	private long count;
	private int created;
	public StockGoodsStoreLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StockGoodsStoreLog(long goodsId, String goodsName,Long merchantId,String merchantName,String memo, Float price,long count, int created) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.merchantId = merchantId;
		this.merchantName = merchantName;
		this.memo = memo;
		this.price = price;
		this.count = count;
		this.created = created;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
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
	
	
}
