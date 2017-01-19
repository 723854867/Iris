package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemAccountSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemAccountMapper {

	@SelectProvider(type = MemAccountSQLBuilder.class, method = "getByAccount")
	MemAccount getByAccount(String account);
	
	@InsertProvider(type = MemAccountSQLBuilder.class, method = "insert")
	void insert(MemAccount account);
	
	@SelectProvider(type = MemAccountSQLBuilder.class, method = "getByMerchantId")
	List<MemAccount> getByMerchantId(long merchantId);
}
