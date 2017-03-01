package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.StockGoodsStoreLog;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.StockGoodsStoreLogSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface StockGoodsStoreLogMapper {

	@InsertProvider(type = StockGoodsStoreLogSQLBuilder.class , method = "insert")
	void insert(StockGoodsStoreLog log);
	
	@SelectProvider(type = StockGoodsStoreLogSQLBuilder.class , method = "getLogList")
	List<StockGoodsStoreLog> getLogList(@Param("goodsId")long goodsId,@Param("merchantId") long merchantId);
	
	@SelectProvider(type = StockGoodsStoreLogSQLBuilder.class , method = "getLogListByGoodsId")
	List<StockGoodsStoreLog> getLogListByGoodsId(@Param("goodsId")long goodsId);
}
