package org.Iris.app.jilu.storage.domain;

public class MemLabelBind {

	private String labelId;
	private long merchantId;
	private int bindType;
	private String bindId;
	private String latitude;
	private String longitude;
	private String address;
	private String bindShow;
	private String memo;
	private int bindTime;
	
	public MemLabelBind() {
	}
	
	public MemLabelBind(String labelId, long merchantId, int bindType, String bindId, String latitude, String longitude,
			String address, String bindShow, String memo, int bindTime) {
		this.labelId = labelId;
		this.merchantId = merchantId;
		this.bindType = bindType;
		this.bindId = bindId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.bindShow = bindShow;
		this.memo = memo;
		this.bindTime = bindTime;
	}



	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}
	public long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}
	public int getBindType() {
		return bindType;
	}
	public void setBindType(int bindType) {
		this.bindType = bindType;
	}
	public String getBindId() {
		return bindId;
	}
	public void setBindId(String bindId) {
		this.bindId = bindId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBindShow() {
		return bindShow;
	}
	public void setBindShow(String bindShow) {
		this.bindShow = bindShow;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public int getBindTime() {
		return bindTime;
	}
	public void setBindTime(int bindTime) {
		this.bindTime = bindTime;
	}
	
}
