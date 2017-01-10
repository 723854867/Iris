package org.Iris.app.jilu.common.bean.enums;

import org.Iris.app.jilu.storage.domain.MemCustomer;

public enum CustomerStatusMod {
	
	ID_BEHIND_UPLOADED(1),				// 身份证反面是否上传
	ID_FRONTAGE_UPLOADED(2);			// 身份证正面是否上传

	private int mod;
	
	private CustomerStatusMod(int mod) {
		this.mod = mod;
	}
	
	public int mod() {
		return mod;
	}
	
	public static final boolean hasIdBehind(MemCustomer customer) {
		int statusMod = customer.getStatusMod();
		return (statusMod & ID_BEHIND_UPLOADED.mod) == ID_BEHIND_UPLOADED.mod;
	}
	
	public static final boolean hasIdFrontage(MemCustomer customer) {
		int statusMod = customer.getStatusMod();
		return (statusMod & ID_FRONTAGE_UPLOADED.mod) == ID_FRONTAGE_UPLOADED.mod;
	}
}
