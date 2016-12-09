package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getById")
	MemMerchant getByMerchantId(long merchantId);
	
	void insert(MemMerchant memMerchant);
	
	void update(MemMerchant memMerchant);
}
