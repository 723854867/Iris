package org.Iris.app.jilu.common.bean.enums;

import org.Iris.app.jilu.storage.domain.MemMerchant;

public enum MerchantStatusMod {

	QUALIFIED(1);			// 正式商户，刚刚注册的商户属于游客，完善资料之后变成正式商户
	
	private int mod;
	
	private MerchantStatusMod(int mod) {
		this.mod = mod;
	}
	
	public int mod() {
		return mod;
	}
	
	public static final boolean isQualified(MemMerchant merchant) {
		int statusMod = merchant.getStatusMod();
		return (statusMod & QUALIFIED.mod) == QUALIFIED.mod;
	}
}
