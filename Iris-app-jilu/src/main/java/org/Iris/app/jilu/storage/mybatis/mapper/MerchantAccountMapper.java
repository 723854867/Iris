package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MerchantAccount;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantAccountSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface MerchantAccountMapper {

	@SelectProvider(type = MerchantAccountSQLBuilder.class, method = "getByAccount")
	MerchantAccount getByAccount(String account);
	
	@InsertProvider(type = MerchantAccountSQLBuilder.class, method = "insert")
	void insert(MerchantAccount account);
}
