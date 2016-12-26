package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantAccountSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemAccountMapper {

	@SelectProvider(type = MerchantAccountSQLBuilder.class, method = "getByAccount")
	MemAccount getByAccount(String account);
	
	@InsertProvider(type = MerchantAccountSQLBuilder.class, method = "insert")
	void insert(MemAccount account);
}
