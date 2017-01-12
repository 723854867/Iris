package org.Iris.app.jilu.common.bean.model;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;

/**
 * 商户获取的转单申请列表模块
 * 
 * @author 樊水东 2016年12月23日
 */
public class OrderChangeModel {

	private String orderId;
	private String superMerchantName;
	private long superMerhcantId;
	private int status;
	private List<MemOrderGoods> changeOrderGoodsLilst;

	public OrderChangeModel() {
		super();
	}

	public OrderChangeModel(String orderId,String superMerchantName, long superMerhcantId, int status, List<MemOrderGoods> changeOrderGoodsLilst) {
		this.orderId = orderId;
		this.superMerchantName = superMerchantName;
		this.superMerhcantId = superMerhcantId;
		this.status = status;
		this.changeOrderGoodsLilst = changeOrderGoodsLilst;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSuperMerchantName() {
		return superMerchantName;
	}

	public void setSuperMerchantName(String superMerchantName) {
		this.superMerchantName = superMerchantName;
	}

	public long getSuperMerhcantId() {
		return superMerhcantId;
	}

	public void setSuperMerhcantId(long superMerhcantId) {
		this.superMerhcantId = superMerhcantId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<MemOrderGoods> getChangeOrderGoodsLilst() {
		return changeOrderGoodsLilst;
	}

	public void setChangeOrderGoodsLilst(List<MemOrderGoods> changeOrderGoodsLilst) {
		this.changeOrderGoodsLilst = changeOrderGoodsLilst;
	}

	
}