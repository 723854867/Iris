package org.Iris.app.jilu.common.bean.model;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;

/**
 * 商户获取的转单申请列表模块
 * 
 * @author 樊水东 2016年12月23日
 */
public class OrderChangeModel {

	private String changeMerchantName;
	private List<MerchantOrderGoods> changeOrderGoodsLilst;

	public OrderChangeModel() {
		super();
	}

	public OrderChangeModel(String changeMerchantName, List<MerchantOrderGoods> changeOrderGoodsLilst) {
		super();
		this.changeMerchantName = changeMerchantName;
		this.changeOrderGoodsLilst = changeOrderGoodsLilst;
	}

	public String getChangeMerchantName() {
		return changeMerchantName;
	}

	public void setChangeMerchantName(String changeMerchantName) {
		this.changeMerchantName = changeMerchantName;
	}

	public List<MerchantOrderGoods> getChangeOrderGoodsLilst() {
		return changeOrderGoodsLilst;
	}

	public void setChangeOrderGoodsLilst(List<MerchantOrderGoods> changeOrderGoodsLilst) {
		this.changeOrderGoodsLilst = changeOrderGoodsLilst;
	}
}