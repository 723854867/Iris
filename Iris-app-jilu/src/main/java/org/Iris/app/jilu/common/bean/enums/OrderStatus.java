package org.Iris.app.jilu.common.bean.enums;

import com.aliyun.oss.OSSClient;

/**
 * 订单状态
 * @author FANSD
 *
 */
public enum OrderStatus {
	/**
	 * 创建
	 */
	CREATE(0,"创建"),
	/**
	 * 转单
	 */
	TRANSFORM(1,"转单"),
	/**
	 * 接收转单中
	 */
	RECEIVE_TRANSFORM(2,"接收转单中"),
	/**
	 * 打包中
	 */
	PACKET(3,"打包中"),
	/**
	 * 运输中
	 */
	TRANSPORT(4,"运输中"),
	/**
	 * 完成
	 */
	FINISH(5,"完成"),
	/**
	 * 售后
	 */
	CUSTOMER_SERVICE(6,"售后"),
	/**
	 * 删除（作废）
	 */
	DELETE(9,"作废"),
	/**
	 * 转单成功（该状态针对订单的产品）
	 */
	TRANSFROM_SUCCESS(7,"转单成功");
	
	private int status;
	private String desc;
	private OrderStatus(int status,String desc){
		this.status = status;
		this.desc = desc;
	}
	public int status() {
		return status;
	}
	public String desc(){
		return desc;
	}
	
	public static String getDesc(int status){
		for(OrderStatus os : OrderStatus.values()){
			if(os.status == status)
				return os.desc;
		}
		return "";
	}
}
