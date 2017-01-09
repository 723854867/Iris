package com.busap.vcs.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_info")
public class DeviceInfo implements Serializable {

	private static final long serialVersionUID = -5809289924867925062L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "uuid",columnDefinition = "varchar(50) NOT NULL ",nullable=false)
	private String uuid;		//我拍唯一标识
	
	@Column(name = "uniqueMark",columnDefinition = "varchar(50) NOT NULL ",nullable=false)
	private String uniqueMark;   //设备唯一标识
	
	private Long uid;  //如果是注册用户，关联用户id
	
	@Column(name = "platform",columnDefinition = "varchar(50)")
	private String platform;  //平台：ios,android
	
	@Column(name = "osVersion",columnDefinition = "varchar(50)")
	private String osVersion;  //系统版本
	
	@Column(name = "operator",columnDefinition = "varchar(50)")
	private String operator; // 运营商
	
	@Column(name = "model",columnDefinition = "varchar(50)")
	private String model;  //产品型号
	
	@Column(name = "factory",columnDefinition = "varchar(50)")
	private String factory;  //厂商,针对android
	
	@Column(name = "imei",columnDefinition = "varchar(50)")
	private String imei;  //imei
	
	@Column(name = "mac",columnDefinition = "varchar(50)")
	private String mac;   //mac地址
	
	@Column(name = "iccid",columnDefinition = "varchar(50)")
	private String iccid;  //针对于ios
	
	@Column(name = "meid",columnDefinition = "varchar(50)")
	private String meid;   //针对于ios
	
	@Column(name = "pseudoUniqeId",columnDefinition = "varchar(50)")
	private String pseudoUniqeId; //针对于android
	
	@Column(name = "androidId",columnDefinition = "varchar(50)")
	private String androidId; //针对于android
	
	@Column(name = "wlanMac",columnDefinition = "varchar(50)")
	private String wlanMac;//针对于android
	
	@Column(name = "btMac",columnDefinition = "varchar(50)")
	private String btMac;//针对于android
	
	@Column(name = "cpu",columnDefinition = "varchar(50)")
	private String cpu; //cpu,针对android
	
	@Column(name = "memory",columnDefinition = "varchar(50)")
	private String memory;   //内存，针对android
	
	@Column(name = "screenResolution",columnDefinition = "varchar(50)")
	private String screenResolution;  //屏幕分辨率，针对android
	
	@Column(name = "pixel",columnDefinition = "varchar(50)")
	private String pixel; //摄像头像素，针对android

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUniqueMark() {
		return uniqueMark;
	}

	public void setUniqueMark(String uniqueMark) {
		this.uniqueMark = uniqueMark;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getPseudoUniqeId() {
		return pseudoUniqeId;
	}

	public void setPseudoUniqeId(String pseudoUniqeId) {
		this.pseudoUniqeId = pseudoUniqeId;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public String getWlanMac() {
		return wlanMac;
	}

	public void setWlanMac(String wlanMac) {
		this.wlanMac = wlanMac;
	}

	public String getBtMac() {
		return btMac;
	}

	public void setBtMac(String btMac) {
		this.btMac = btMac;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getScreenResolution() {
		return screenResolution;
	}

	public void setScreenResolution(String screenResolution) {
		this.screenResolution = screenResolution;
	}

	public String getPixel() {
		return pixel;
	}

	public void setPixel(String pixel) {
		this.pixel = pixel;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
		
}
