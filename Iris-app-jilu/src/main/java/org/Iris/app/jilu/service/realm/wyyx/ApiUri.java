package org.Iris.app.jilu.service.realm.wyyx;

/**
 * 接口地址
 * @author 樊水东
 * 2016年10月19日
 */
public enum ApiUri {
	/*
	 * 创建云信id
	 */
	CREATE_YXID("http","api.netease.im",80,"/nimserver/user/create.action");
	
	private String http;
	private String ip;
	private int port;
	private String path;
	
	private ApiUri(String http,String ip,int port,String path){
		this.http = http;
		this.ip = ip;
		this.port = port;
		this.path = path;
	}

	public String getHttp() {
		return http;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getPath() {
		return path;
	}

	public void setHttp(String http) {
		this.http = http;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
}
