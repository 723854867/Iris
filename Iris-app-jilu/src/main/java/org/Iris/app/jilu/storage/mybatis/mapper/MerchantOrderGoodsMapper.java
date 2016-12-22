package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantOrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MerchantOrderGoodsMapper {

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "insert")
	void insert(MerchantOrderGoods order);

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(List<MerchantOrderGoods> list);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "update")
	void update(MerchantOrderGoods order);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(List<MerchantOrderGoods> list);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "delete")
	void delete(long id);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchDelete")
	void batchDelete(List<Long> list);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsById")
	MerchantOrderGoods getMerchantOrderGoodsById(long id);
	
	@SelectProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsByIdList")
	List<MerchantOrderGoods> getMerchantOrderGoodsByIdList(List<Long> list);
}
