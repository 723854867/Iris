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
	
	@SelectProvider(type = MemGoodsStoreSQLBuilder.class, method = "getCountBymerchantId")
	long getCountBymerchantId(long merchantId);
	
	@SelectProvider(type = MemGoodsStoreSQLBuilder.class, method = "getMemGoodsStoreList")
	List<MemGoodsStore> getMemGoodsStoreList(@Param("start")int page,@Param("pageSize")int pageSize,@Param("merchantId")long merhcantId);
}
