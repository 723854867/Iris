package org.Iris.app.jilu.common.bean.model;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;

/**
 * 商户正在转单的订单列表模块
 * 
 * @author 樊水东 2016年12月23日
 */
public class TransferOrderModel {

	private String orderId;
	private String changeMerhcantName;
	private long changeMerhcantId;
	private List<MemOrderGoods> transferOrderGoodsLilst;

	public TransferOrderModel() {
		super();
	}

	public TransferOrderModel(String orderId, String changeMerhcantName, long changeMerhcantId,
			List<MemOrderGoods> transferOrderGoodsLilst) {
		super();
		this.orderId = orderId;
		this.changeMerhcantName = changeMerhcantName;
		this.changeMerhcantId = changeMerhcantId;
		this.transferOrderGoodsLilst = transferOrderGoodsLilst;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChangeMerhcantName() {
		return changeMerhcantName;
	}

	public void setChangeMerhcantName(String changeMerhcantName) {
		this.changeMerhcantName = changeMerhcantName;
	}

	public long getChangeMerhcantId() {
		return changeMerhcantId;
	}

	public void setChangeMerhcantId(long changeMerhcantId) {
		this.changeMerhcantId = changeMerhcantId;
	}

	public List<MemOrderGoods> getTransferOrderGoodsLilst() {
		return transferOrderGoodsLilst;
	}

	public void setTransferOrderGoodsLilst(List<MemOrderGoods> transferOrderGoodsLilst) {
		this.transferOrderGoodsLilst = transferOrderGoodsLilst;
	}

}