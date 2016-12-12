package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getByMerchantId")
	MemMerchant getByMerchantId(long merchantId);
	
	@InsertProvider(type = MemMerchantSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "merchant_id", keyProperty = "merchantId")
	void insert(MemMerchant memMerchant);
	
	void update(MemMerchant memMerchant);
}
