package org.Iris.app.jilu.common.http;

/**
 * 接口地址
 * @author 樊水东
 * 2016年10月19日
 */
public enum ApiUri {
	/**
	 * 创建云信id
	 */
	CREATE_YXID("https","api.netease.im","/nimserver/user/create.action"),
	/**
	 * 更新云信Id
	 */
	UPDATE_YXID("https","api.netease.im","/nimserver/user/update.action"),
	/**
	 * 更新并获取云信id
	 */
	REFRESH_YXID("https","api.netease.im","/nimserver/user/refreshToken.action"),
	/**
	 * 获取微信第三方登陆token
	 */
	GET_WEIXIN_LOGIN_TOKEN("https","api.weixin.qq.com","/sns/oauth2/access_token"),
	/**
	 * 刷新token
	 */
	WEIXIN_REFRESH_TOKEN("https","api.weixin.qq.com","/sns/oauth2/refresh_token");
	
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
