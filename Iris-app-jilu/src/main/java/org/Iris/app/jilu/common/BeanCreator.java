package org.Iris.app.jilu.common;

import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.storage.domain.MemMerchant;

public class BeanCreator {

	public static MemMerchant newMemMerchant(AccountType type, String account, String avatar, String address) { 
		MemMerchant memMerchant = new MemMerchant();
		return memMerchant;
	}
}
