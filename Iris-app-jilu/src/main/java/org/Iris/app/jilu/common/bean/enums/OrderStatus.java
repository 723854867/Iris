package org.Iris.app.jilu.common.bean.enums;
/**
 * 订单状态
 * @author FANSD
 *
 */
public enum OrderStatus {
	/**
	 * 创建
	 */
	CREATE(0),
	/**
	 * 转单
	 */
	TRANSFORM(1),
	/**
	 * 接收转单中
	 */
	RECEIVE_TRANSFORM(2),
	/**
	 * 打包中
	 */
	PACKET(3),
	/**
	 * 运输中
	 */
	TRANSPORT(4),
	/**
	 * 完成
	 */
	FINISH(5),
	/**
	 * 售后
	 */
	CUSTOMER_SERVICE(6),
	/**
	 * 删除（作废）
	 */
	DELETE(9),
	/**
	 * 转单成功（该状态针对订单的产品）
	 */
	TRANSFROM_SUCCESS(7);
	
	private int status;
	private OrderStatus(int status){
		this.status = status;
	}
	public int status() {
		return status;
	}
}
