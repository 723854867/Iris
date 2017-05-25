package org.Iris.app.jilu.storage.domain;

import org.Iris.util.lang.DateUtils;

public class MemFeedBack {
	private long feedbackId;
	private int created;
	private long merchantId;
	private String content;
	private int isdealed;
	private int dealTime;
	private String contact;
	
	public MemFeedBack(){
		
	}
	
	public MemFeedBack(String content,String contact,long merchantId){
		this.contact = contact;
		this.content = content;
		this.merchantId = merchantId;
		this.created = DateUtils.currentTime();
	}

	public long getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(long feedbackId) {
		this.feedbackId = feedbackId;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsdealed() {
		return isdealed;
	}

	public void setIsdealed(int isdealed) {
		this.isdealed = isdealed;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public int getDealTime() {
		return dealTime;
	}

	public void setDealTime(int dealTime) {
		this.dealTime = dealTime;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	

}
