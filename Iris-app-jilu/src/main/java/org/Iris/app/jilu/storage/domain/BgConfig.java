package org.Iris.app.jilu.storage.domain;
/**
 * 后台配置实体类
 * @author 樊水东
 * 2017年2月17日
 */
public class BgConfig {

	private String key;
	private String value;
	
	public BgConfig() {
		super();
	}
	
	public BgConfig(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
