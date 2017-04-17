package org.Iris.app.jilu.common.bean.enums;

/**
 * 支付订单状态
 * @author 樊水东
 * 2017年4月17日
 */
public enum PayOrderStatus {

	WAIT_PAY(0),
	PAY_SUCCESS(1),
	PAY_REFUND(2),
	CLOSE(3);
	
	private int status;
	
	private PayOrderStatus(int status){
		this.status = status;
	}
	
	public int status(){
		return status;
	}
}
