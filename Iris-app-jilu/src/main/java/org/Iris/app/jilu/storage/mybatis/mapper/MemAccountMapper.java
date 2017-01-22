package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemAccount;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemAccountSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemAccountMapper {

	@SelectProvider(type = MemAccountSQLBuilder.class, method = "getByAccount")
	MemAccount getByAccount(String account);
	
	@SelectProvider(type = MemAccountSQLBuilder.class, method = "getByMerchantIdAndType")
	MemAccount getByMerchantIdAndType(@Param("merchantId")long merchantId,@Param("type") int type);
	
	@InsertProvider(type = MemAccountSQLBuilder.class, method = "insert")
	void insert(MemAccount account);
	
	@UpdateProvider(type = MemAccountSQLBuilder.class, method = "update")
	void update(MemAccount account);
	
	@SelectProvider(type = MemAccountSQLBuilder.class, method = "getByMerchantId")
	List<MemAccount> getByMerchantId(long merchantId);
}
