package org.Iris.app.jilu.common.bean.enums;
/**
 * 推送类型
 * @author 樊水东
 * 2017年2月10日
 */
public enum IgtPushType {

	/**
	 * 转单推送
	 */
	ORDER_TRANSFORM(0),
	/**
	 * 转单被取消
	 */
	ORDER_CANCEL(1),
	/**
	 * 转单被接受
	 */
	ORDER_RECEIVE(2),
	/**
	 * 转单被拒绝
	 */
	ORDER_REFUSE(3),
	/**
	 * 订单状态改变
	 */
	ORDER_STATUS_CHANGE(4),
	FRIEND_APPLY(5),
	FRIEND_APPLY_REPLY(6);
	
	private int mark;
	
	private IgtPushType(int mark){
		this.mark = mark;
	}
	
	public int mark(){
		return this.mark;
	}
}
