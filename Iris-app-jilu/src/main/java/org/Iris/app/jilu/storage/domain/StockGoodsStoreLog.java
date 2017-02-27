package org.Iris.app.jilu.storage.domain;
/**
 * 进货日志
 * @author fansd
 *
 */
public class StockGoodsStoreLog {
	private long goodsId;
	private String memo;
	private long count;
	private int created;
	public StockGoodsStoreLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StockGoodsStoreLog(long goodsId, String memo, long count, int created) {
		super();
		this.goodsId = goodsId;
		this.memo = memo;
		this.count = count;
		this.created = created;
	}
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
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
	
	
}
