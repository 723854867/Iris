package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getById")
	MemMerchant getById(long merchat_id);
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getByAccount")
	MemMerchant getByAccount(@Param("type") int type, @Param("account") String account);
	@InsertProvider(type = MemMerchantSQLBuilder.class, method = "insert")
	void insert(MemMerchant memUser);
	@UpdateProvider(type = MemMerchantSQLBuilder.class, method = "update")
	void update(MemMerchant memUser);
}
