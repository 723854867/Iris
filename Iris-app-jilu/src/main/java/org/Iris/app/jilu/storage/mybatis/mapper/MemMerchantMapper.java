package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemMerchantSQLBuilder;
<<<<<<< HEAD
import org.Iris.app.jilu.storage.mybatis.domain.MemMerchant;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
=======
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
>>>>>>> 9e0c873049238851849e3e9d27e689741acc69ba
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemMerchantMapper {
	
<<<<<<< HEAD
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getById")
	MemMerchant getById(long merchat_id);
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getByAccount")
	MemMerchant getByAccount(@Param("type") int type, @Param("account") String account);
	@InsertProvider(type = MemMerchantSQLBuilder.class, method = "insert")
	void insert(MemMerchant memUser);
	@UpdateProvider(type = MemMerchantSQLBuilder.class, method = "update")
	void update(MemMerchant memUser);
=======
	@SelectProvider(type = MemMerchantSQLBuilder.class, method = "getByMerchantId")
	MemMerchant getByMerchantId(long merchantId);
	
	@InsertProvider(type = MemMerchantSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "merchant_id", keyProperty = "merchantId")
	void insert(MemMerchant memMerchant);
	
	void update(MemMerchant memMerchant);
>>>>>>> 9e0c873049238851849e3e9d27e689741acc69ba
}
