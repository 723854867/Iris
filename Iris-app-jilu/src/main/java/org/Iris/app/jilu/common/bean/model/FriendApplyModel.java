package org.Iris.app.jilu.common.bean.model;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.util.lang.DateUtils;

public class FriendApplyModel {

	private long applier;
	private String applierName;
	private String memo;
	private String timestamp;
	
	public FriendApplyModel() {}
	
	public FriendApplyModel(MemMerchant applier, String memo) {
		this.memo = memo;
		this.applierName = applier.getName();
		this.applier = applier.getMerchantId();
		this.timestamp = DateUtils.getUTCDate();
	}

	public long getApplier() {
		return applier;
	}

	public void setApplier(long applier) {
		this.applier = applier;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
