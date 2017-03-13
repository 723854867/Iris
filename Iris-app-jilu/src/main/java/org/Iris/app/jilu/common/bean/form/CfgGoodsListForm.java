package org.Iris.app.jilu.common.bean.form;

import java.util.List;

/**
 * 商品列表模板
 * @author 樊水东
 * 2017年3月13日
 */
public class CfgGoodsListForm {

	private String goodsCode;
	private List<CfgGoodsForm> list;
	private int count;
	
	public CfgGoodsListForm(String goodsCode, List<CfgGoodsForm> list) {
		super();
		this.goodsCode = goodsCode;
		this.list = list;
		this.count = list.size();
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public List<CfgGoodsForm> getList() {
		return list;
	}
	public void setList(List<CfgGoodsForm> list) {
		this.list = list;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}
