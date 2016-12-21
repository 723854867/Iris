package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MerchantOrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MerchantOrderGoodsMapper {

	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "insert")
	void insert(MerchantOrderGoods order);

	@InsertProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(Map<String, List<MerchantOrderGoods>> map);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "update")
	void update(MerchantOrderGoods order);

	@UpdateProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(Map<String, List<MerchantOrderGoods>> map);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "delete")
	void delete(long id);

	@DeleteProvider(type = MerchantOrderGoodsSQLBuilder.class, method = "batchDelete")
	void batchDelete(Map<String, List<Long>> map);
}
