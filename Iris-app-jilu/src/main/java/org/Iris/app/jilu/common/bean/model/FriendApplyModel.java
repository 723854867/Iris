package org.Iris.app.jilu.common.bean.model;

import org.Iris.app.jilu.service.realm.merchant.Merchant;

public class FriendApplyModel {

	private long applier;
	private String applierName;
	private String memo;
	
	public FriendApplyModel() {}
	
	public FriendApplyModel(Merchant applier, String memo) {
		this.memo = memo;
		this.applierName = applier.getMemMerchant().getName();
		this.applier = applier.getMemMerchant().getMerchantId();
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
}
