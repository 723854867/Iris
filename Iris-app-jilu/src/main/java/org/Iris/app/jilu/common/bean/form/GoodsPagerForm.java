package org.Iris.app.jilu.common.bean.form;

import org.Iris.app.jilu.storage.domain.CfgGoods;

public class GoodsPagerForm {

	private long goodsId;
	private String goodsCode;
	private String zhName;
	private String goodsFormat;
	
	public GoodsPagerForm(CfgGoods goods){
		this.goodsId = goods.getGoodsId();
		this.goodsCode = goods.getGoodsCode();
		this.zhName = goods.getZhName();
		this.goodsFormat = goods.getGoodsFormat();
	}
	
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getZhName() {
		return zhName;
	}
	public void setZhName(String zhName) {
		this.zhName = zhName;
	}
	public String getGoodsFormat() {
		return goodsFormat;
	}
	public void setGoodsFormat(String goodsFormat) {
		this.goodsFormat = goodsFormat;
	}
	
}
