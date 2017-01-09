package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "probe")
// 探针
public class Probe extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7502364576619450659L;
	
	private String localOutIP;		//本地出口IP
	
	@Column(name = "traceroute_list",columnDefinition = "varchar(512)  NULL ", nullable=true)
	private String tracerouteList;	//路由跟踪
	
	@Column(name = "dnslist",columnDefinition = "varchar(512)  NULL ", nullable=true)
	private String DNSList;			//DNS列表
	
	private String TargetDNS;		//链接的DNS
	
	@Column(name = "ping_log",columnDefinition = "varchar(512)  NULL ", nullable=true)
	private String pingLog;			//ping记录
	
	private String publishHost;		//发包域名
	
	private String piliHost;		//接包域名
	
	private String publishSpeed;	//发包速度
	
	private String piliSpeed;		//接包速度	
	
	private String fastestPingSpeed;//最快ping时间

	
	@Column(name = "fullLog",columnDefinition = "varchar(4096)  NULL ", nullable=true)
	private String fullLog;			//完整log
	
	
	private String pingTotal;		//ping总次数
	
	private String pingAvgTimeout;		//ping平均超时时间
	
	private String pingMaxTimeout;		//ping最大超时时间
	
	private String pingTimeoutCount;		//ping超时次数
	
	private String clientType;		//客户端类型
	
	
	public String getLocalOutIP() {
		return localOutIP;
	}

	public void setLocalOutIP(String localOutIP) {
		this.localOutIP = localOutIP;
	}

	public String getTracerouteList() {
		return tracerouteList;
	}

	public void setTracerouteList(String tracerouteList) {
		this.tracerouteList = tracerouteList;
	}

	public String getDNSList() {
		return DNSList;
	}

	public void setDNSList(String dNSList) {
		DNSList = dNSList;
	}

	public String getTargetDNS() {
		return TargetDNS;
	}

	public void setTargetDNS(String targetDNS) {
		TargetDNS = targetDNS;
	}

	public String getPingLog() {
		return pingLog;
	}

	public void setPingLog(String pingLog) {
		this.pingLog = pingLog;
	}

	public String getPublishHost() {
		return publishHost;
	}

	public void setPublishHost(String publishHost) {
		this.publishHost = publishHost;
	}

	public String getPiliHost() {
		return piliHost;
	}

	public void setPiliHost(String piliHost) {
		this.piliHost = piliHost;
	}

	public String getPublishSpeed() {
		return publishSpeed;
	}

	public void setPublishSpeed(String publishSpeed) {
		this.publishSpeed = publishSpeed;
	}

	public String getPiliSpeed() {
		return piliSpeed;
	}

	public void setPiliSpeed(String piliSpeed) {
		this.piliSpeed = piliSpeed;
	}

	public String getFullLog() {
		return fullLog;
	}

	public void setFullLog(String fullLog) {
		this.fullLog = fullLog;
	}

	public String getFastestPingSpeed() {
		return fastestPingSpeed;
	}

	public void setFastestPingSpeed(String fastestPingSpeed) {
		this.fastestPingSpeed = fastestPingSpeed;
	}

	public String getPingTotal() {
		return pingTotal;
	}

	public void setPingTotal(String pingTotal) {
		this.pingTotal = pingTotal;
	}

	public String getPingAvgTimeout() {
		return pingAvgTimeout;
	}

	public void setPingAvgTimeout(String pingAvgTimeout) {
		this.pingAvgTimeout = pingAvgTimeout;
	}

	public String getPingMaxTimeout() {
		return pingMaxTimeout;
	}

	public void setPingMaxTimeout(String pingMaxTimeout) {
		this.pingMaxTimeout = pingMaxTimeout;
	}

	public String getPingTimeoutCount() {
		return pingTimeoutCount;
	}

	public void setPingTimeoutCount(String pingTimeoutCount) {
		this.pingTimeoutCount = pingTimeoutCount;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
	
	
	

}
