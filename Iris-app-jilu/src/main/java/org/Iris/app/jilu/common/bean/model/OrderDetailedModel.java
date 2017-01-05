package org.Iris.app.jilu.common.bean.model;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.OrderForm;
import org.Iris.app.jilu.common.bean.form.OrderGoodsForm;
import org.Iris.app.jilu.common.bean.form.OrderPacketForm;

/**
 * 订单详细信息模板
 * 订单详细信息分为以下5个部分
 * 1：订单基本信息
 * 2：订单未完成部分
 * 3：订单正在转单部分
 * 4：订单打包部分
 * 5：子订单基本信息
 * @author 樊水东
 * 2017年1月5日
 */
public class OrderDetailedModel {

	private OrderForm orderInfo;
	private List<OrderGoodsForm> notFinishGoodsList;
	private List<OrderGoodsForm> changeGoodsList;
	private List<OrderPacketForm> packetList;
	private List<OrderForm> childOrderList;
	
	public OrderDetailedModel() {
		// TODO Auto-generated constructor stub
	}
	public OrderDetailedModel(OrderForm orderInfo, List<OrderGoodsForm> notFinishGoodsList,
			List<OrderGoodsForm> changeGoodsList, List<OrderPacketForm> packetList, List<OrderForm> childOrderList) {
		super();
		this.orderInfo = orderInfo;
		this.notFinishGoodsList = notFinishGoodsList;
		this.changeGoodsList = changeGoodsList;
		this.packetList = packetList;
		this.childOrderList = childOrderList;
	}
	public OrderForm getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(OrderForm orderInfo) {
		this.orderInfo = orderInfo;
	}
	public List<OrderGoodsForm> getNotFinishGoodsList() {
		return notFinishGoodsList;
	}
	public void setNotFinishGoodsList(List<OrderGoodsForm> notFinishGoodsList) {
		this.notFinishGoodsList = notFinishGoodsList;
	}
	public List<OrderGoodsForm> getChangeGoodsList() {
		return changeGoodsList;
	}
	public void setChangeGoodsList(List<OrderGoodsForm> changeGoodsList) {
		this.changeGoodsList = changeGoodsList;
	}
	public List<OrderPacketForm> getPacketList() {
		return packetList;
	}
	public void setPacketList(List<OrderPacketForm> packetList) {
		this.packetList = packetList;
	}
	public List<OrderForm> getChildOrderList() {
		return childOrderList;
	}
	public void setChildOrderList(List<OrderForm> childOrderList) {
		this.childOrderList = childOrderList;
	}
	
	
	
}
