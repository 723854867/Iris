package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

public class MemLabelBind {

	private String labelId;
	private long merchantId;
	private int status;
	private int bindType;
	private String bindId;
	private long buyId;//用户购买的日志id 
	private int created;
	private int updated;
	
	public MemLabelBind() {
	}
	public MemLabelBind(String labelId, long merchantId,long buyId) {
		int time = DateUtils.currentTime();
		this.labelId = labelId;
		this.merchantId = merchantId;
		this.buyId = buyId;
		this.created = time;
		this.updated = time;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	
	public long getBuyId() {
		return buyId;
	}
	public void setBuyId(long buyId) {
		this.buyId = buyId;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}
	
	
	
}
