package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemMerchantMapper {
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getByMerchantId")
	MemMerchant getByMerchantId(long merchantId);
	
	@InsertProvider(type = MemMerchantSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "merchant_id", keyProperty = "merchantId")
	void insert(MemMerchant merchant);
	
	@UpdateProvider(type = MemMerchantSQLBuilder.class, method = "update")
	void update(MemMerchant merchant);
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "list")
	List<MemMerchant> list(Map<String, Object> map);
	
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "count")
	long count(Map<String, Object> map);
}
