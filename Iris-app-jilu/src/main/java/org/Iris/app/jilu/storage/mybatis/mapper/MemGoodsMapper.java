package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemGoodsSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemGoodsMapper {

	@InsertProvider(type = MemGoodsSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "goods_id", keyProperty = "goodsId")
	void insert(MemGoods memGoods);

	@SelectProvider(type = MemGoodsSQLBuilder.class, method = "getGoodsById")
	MemGoods getGoodsById(long goodsId);
}
