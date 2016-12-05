package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getById")
	MemMerchant getById(long uid);
	
	MemMerchant getByAccount(@Param("type") int type, @Param("account") String account);
	
	void insert(MemMerchant memUser);
	
	void update(MemMerchant memUser);
}
