package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemGoodsStoreSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemGoodsStoreMapper {

	@InsertProvider(type = MemGoodsStoreSQLBuilder.class, method = "insert")
	void insert(MemGoodsStore store);
	
	@UpdateProvider(type = MemGoodsStoreSQLBuilder.class, method = "update")
	void update(MemGoodsStore store);
	
	@InsertProvider(type = MemGoodsStoreSQLBuilder.class, method = "batchInsert")
	void batchInsert(List<MemGoodsStore> list);
	
	@SelectProvider(type = MemGoodsStoreSQLBuilder.class, method = "getMemGoodsStoreById")
	MemGoodsStore getMemGoodsStoreById(@Param("merchantId") long merchantId,@Param("goodsId") long goodsId);
}
