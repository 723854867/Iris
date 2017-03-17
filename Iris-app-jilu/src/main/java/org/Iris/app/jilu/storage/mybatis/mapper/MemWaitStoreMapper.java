package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemWaitStore;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemWaitStoreSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemWaitStoreMapper {

	@Options(useGeneratedKeys = true , keyColumn = "id" , keyProperty = "id")
	@InsertProvider(type = MemWaitStoreSQLBuilder.class , method = "insert")
	void insert(MemWaitStore store);
	
	@UpdateProvider(type = MemWaitStoreSQLBuilder.class , method = "update")
	void update(MemWaitStore store);
	
	@InsertProvider(type = MemWaitStoreSQLBuilder.class , method = "batchInsert")
	void batchInsert(List<MemWaitStore> list);
	
	@UpdateProvider(type = MemWaitStoreSQLBuilder.class , method = "batchUpdate")
	void batchUpdate(List<MemWaitStore> list);
	
	@SelectProvider(type = MemWaitStoreSQLBuilder.class , method = "find")
	MemWaitStore find(@Param("orderId") String orderId,@Param("merchantId") long merchantId,@Param("goodsId") long goodsId);
}
