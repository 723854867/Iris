package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderGoodsMapper {

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MemOrderGoodsSQLBuilder.class, method = "insert")
	void insert(MemOrderGoods order);

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(Map<String, List<MemOrderGoods>> map);

	@UpdateProvider(type = MemOrderGoodsSQLBuilder.class, method = "update")
	void update(MemOrderGoods order);

	@UpdateProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(Map<String, List<MemOrderGoods>> map);

	@DeleteProvider(type = MemOrderGoodsSQLBuilder.class, method = "delete")
	void delete(long id);

	@DeleteProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchDelete")
	void batchDelete(Map<String, List<Long>> map);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getMemOrderGoodsById")
	MemOrderGoods getMemOrderGoodsById(long id);
}
