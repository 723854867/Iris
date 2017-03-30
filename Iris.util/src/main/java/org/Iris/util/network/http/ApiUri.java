package org.Iris.util.network.http;

/**
 * 接口地址
 * @author 樊水东
 * 2016年10月19日
 */
public enum ApiUri {
	/**
	 * 微信订单查询
	 */
	ORDER_QUERY("https","api.mch.weixin.qq.com","/pay/orderquery"),
	/**
	 * 微信关闭订单
	 */
	CLOSE_ORDER("https","api.mch.weixin.qq.com","/pay/closeorder"),
	/**
	 * 申请退款
	 */
	REFUND("https","api.mch.weixin.qq.com","/secapi/pay/refund"),
	/**
	 * 微信统一下单地址
	 */
	UNIFIED_ORDER("https","api.mch.weixin.qq.com","/pay/unifiedorder");
	private String http;
	private String host;
	private String path;
	
	private ApiUri(String http,String host,String path){
		this.http = http;
		this.host = host;
		this.path = path;
	}

	public String getHttp() {
		return http;
	}

	public String getPath() {
		return path;
	}

	public void setHttp(String http) {
		this.http = http;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	
}
