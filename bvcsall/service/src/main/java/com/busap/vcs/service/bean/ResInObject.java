package com.busap.vcs.service.bean;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResInObject {
	private String busiCode;
	private String requestSource;
	private String transactionID;
	private String signature;
    private String paramsJson;
    private Map<String, String> images=new HashMap<String, String>();
    
	public ResInObject(){
		
	}
	public ResInObject(String busiCode, String requestSource,
			String transactionID, String signature,  String paramsJson) {
		super();
		this.busiCode = busiCode;
		this.requestSource = requestSource;
		this.transactionID = transactionID;
		this.signature = signature;
		this.paramsJson = paramsJson;
	}
	public ResInObject(String busiCode, String requestSource,
			String transactionID, String signature,  String  paramsJson,
			Map<String, String> images) {
		super();
		this.busiCode = busiCode;
		this.requestSource = requestSource;
		this.transactionID = transactionID;
		this.signature = signature;
		this.paramsJson = paramsJson;
		this.images = images;
	}
	public String getBusiCode() {
		return busiCode;
	}
	public void setBusiCode(String busiCode) {
		this.busiCode = busiCode;
	}
	public String getRequestSource() {
		return requestSource;
	}
	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}
	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	 
	public Map<String, String> getImages() {
		return images;
	}
	public void setImages(Map<String, String> images) {
		this.images = images;
	}
	public String getParamsJson() {
		return paramsJson;
	}
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
}
