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
	private int created;
	private List<MemOrderGoods> changeOrderGoodsList;

	public OrderChangeModel() {
		super();
	}

	public OrderChangeModel(String orderId,String superMerchantName, long superMerhcantId, int status,List<MemOrderGoods> changeOrderGoodsList) {
		this.orderId = orderId;
		this.superMerchantName = superMerchantName;
		this.superMerhcantId = superMerhcantId;
		this.created = status;
		this.changeOrderGoodsList = changeOrderGoodsList;
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

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public List<MemOrderGoods> getChangeOrderGoodsList() {
		return changeOrderGoodsList;
	}

	public void setChangeOrderGoodsList(List<MemOrderGoods> changeOrderGoodsList) {
		this.changeOrderGoodsList = changeOrderGoodsList;
	}

}