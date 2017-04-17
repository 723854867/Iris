package org.Iris.app.jilu.common.bean.enums;
/**
 * 支付方式
 * @author 樊水东
 * 2017年4月17日
 */
public enum PayType {
	
	ALIPAY(0),
	WECHAT(1),
	APPLE(2);
	
	private int type;
	
	private PayType(int type){
		this.type = type;
	}

	public int type(){
		return type;
	}
}
