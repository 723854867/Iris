package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CfgGoodsSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

public interface CfgGoodsMapper {

	@InsertProvider(type = CfgGoodsSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "goods_id", keyProperty = "goodsId")
	void insert(CfgGoods memGoods);

	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsById")
	CfgGoods getGoodsById(long goodsId);
}
