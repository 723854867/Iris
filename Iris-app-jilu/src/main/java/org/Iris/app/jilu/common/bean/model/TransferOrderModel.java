package org.Iris.app.jilu.common.bean.model;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.OrderGoodsForm;

/**
 * 商户正在转单的订单列表模块
 * 
 * @author 樊水东 2016年12月23日
 */
public class TransferOrderModel {

	private String orderId;
	private String changeMerchantName;
	private long changeMerchantId;
	private int created;
	private List<OrderGoodsForm> transferOrderGoodsList;

	public TransferOrderModel() {
		super();
	}

	public TransferOrderModel(String orderId, String changeMerchantName, long changeMerchantId,int created,
			List<OrderGoodsForm> transferOrderGoodsList) {
		super();
		this.orderId = orderId;
		this.changeMerchantName = changeMerchantName;
		this.changeMerchantId = changeMerchantId;
		this.created = created;
		this.transferOrderGoodsList = transferOrderGoodsList;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChangeMerchantName() {
		return changeMerchantName;
	}

	public void setChangeMerchantName(String changeMerchantName) {
		this.changeMerchantName = changeMerchantName;
	}

	public long getChangeMerchantId() {
		return changeMerchantId;
	}

	public void setChangeMerchantId(long changeMerchantId) {
		this.changeMerchantId = changeMerchantId;
	}

	public List<OrderGoodsForm> getTransferOrderGoodsList() {
		return transferOrderGoodsList;
	}

	public void setTransferOrderGoodsList(List<OrderGoodsForm> transferOrderGoodsList) {
		this.transferOrderGoodsList = transferOrderGoodsList;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

}