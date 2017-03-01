package org.Iris.app.jilu.common.bean.form;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.JiLuResourceUtil;
import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.domain.StockGoodsStoreLog;

/**
 * 商品仓储信息（包括进货记录）
 * 
 * @author fansd
 *
 */
public class GoodsStoreAndStockForm {

	private long merchantId;
	private String merchantName;
	private long goodsId;
	private String goodsName;
	private long count;
	private float price;
	private String memo;
	private int statusMod;
	private String goodsImage;
	// 进货记录
	private List<StockGoodsStoreLog> stockGoodsStoreLogs;

	public GoodsStoreAndStockForm() {
	}

	public GoodsStoreAndStockForm(MemGoodsStore store, List<StockGoodsStoreLog> stockGoodsStoreLogs) {
		this.merchantId = store.getMerchantId();
		this.merchantName = store.getMerchantName();
		this.goodsId = store.getGoodsId();
		this.goodsName = store.getGoodsName();
		this.count = store.getCount();
		this.price = store.getPrice();
		this.memo = store.getMemo();
		this.statusMod = store.getStatusMod();
		this.goodsImage = JiLuResourceUtil.goodsImageUri(store);
		this.stockGoodsStoreLogs = stockGoodsStoreLogs;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getStatusMod() {
		return statusMod;
	}

	public void setStatusMod(int statusMod) {
		this.statusMod = statusMod;
	}

	public List<StockGoodsStoreLog> getStockGoodsStoreLogs() {
		return stockGoodsStoreLogs;
	}

	public void setStockGoodsStoreLogs(List<StockGoodsStoreLog> stockGoodsStoreLogs) {
		this.stockGoodsStoreLogs = stockGoodsStoreLogs;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}
}
