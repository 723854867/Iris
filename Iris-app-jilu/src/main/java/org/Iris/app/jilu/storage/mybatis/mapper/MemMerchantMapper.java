package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MerchantSQLBuilder.class, method = "getByMerchantId")
	MemMerchant getByMerchantId(long merchantId);
	
	@InsertProvider(type = MerchantSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "merchant_id", keyProperty = "merchantId")
	void insert(MemMerchant merchant);
	
	@UpdateProvider(type = MerchantSQLBuilder.class, method = "update")
	void update(MemMerchant merchant);
}
