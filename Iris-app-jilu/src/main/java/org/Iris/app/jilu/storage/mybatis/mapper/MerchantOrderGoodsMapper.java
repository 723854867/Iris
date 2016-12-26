package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantOrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MerchantOrderGoodsMapper {

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "insert")
	void insert(MemOrderGoods order);

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(List<MemOrderGoods> list);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "update")
	void update(MemOrderGoods order);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(List<MemOrderGoods> list);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "delete")
	void delete(long id);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchDelete")
	void batchDelete(List<MemOrderGoods> list);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsById")
	MemOrderGoods getMerchantOrderGoodsById(long id);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsByOrderId")
	MemOrderGoods getMerchantOrderGoodsByOrderId(@Param("orderId") String orderId,@Param("goodsId") long goodsId);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsByList")
	List<MemOrderGoods> getMerchantOrderGoodsByList(List<MemOrderGoods> list);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getChangeMerchantOrderGoodsByOrderId")
	List<MemOrderGoods> getChangeMerchantOrderGoodsByOrderId(String orderId);
}
