package com.busap.vcs.service.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 统一出参数据类型  
 * @version V 1.2  
 */
public class ResOutObject implements Serializable {
	private static final long serialVersionUID = 3862163719814136067L;
	private String returnCode;
	private String returnMessage;
	private String timeStamp;
	private Map<String, String> bean;
	private List<Map<String, String>> beans;
    
	public ResOutObject() {
		this.bean=new HashMap<String, String>();
		this.returnMessage="sucess";
		this.returnCode="0000";
		this.beans=new ArrayList<Map<String, String>>();
		this.timeStamp=String.valueOf(System.currentTimeMillis());
	}

	public ResOutObject(String returnCode, String returnMessage) {
		this.bean=new HashMap<String, String>();
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
		this.timeStamp=String.valueOf(System.currentTimeMillis());
	}
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public Map<String, String> getBean() {
		return bean;
	}

	public void setBean(Map<String, String> bean) {
		this.bean = bean;
	}

	public List<Map<String, String>> getBeans() {
		return beans;
	}

	public void setBeans(List<Map<String, String>> beans) {
		this.beans = beans;
	}
}
