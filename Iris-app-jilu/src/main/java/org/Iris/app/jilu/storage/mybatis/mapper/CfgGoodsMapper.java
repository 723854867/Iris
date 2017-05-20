package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CfgGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CfgGoodsMapper {

	@InsertProvider(type = CfgGoodsSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "goods_id", keyProperty = "goodsId")
	void insert(CfgGoods memGoods);
	
	@UpdateProvider(type = CfgGoodsSQLBuilder.class, method = "update")
	void update(CfgGoods memGoods);
	
	@DeleteProvider(type = CfgGoodsSQLBuilder.class, method = "delete")
	void delete(CfgGoods memGoods);

	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsById")
	CfgGoods getGoodsById(long goodsId);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getCountByGoodsName")
	long getCountByGoodsName(@Param("zhName") String zhName);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getCountByMerchantId")
	long getCountByMerchantId(long merchantId);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getCountByCode")
	long getCountByCode(String goodsCode);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsListByGoodsName")
	List<CfgGoods> getGoodsListByGoodsName(@Param("start")int page,@Param("pageSize")int pageSize,@Param("zhName")String zhName);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsListByMerchantId")
	List<CfgGoods> getGoodsListByMerchantId(@Param("start")int page,@Param("pageSize")int pageSize,@Param("merchantId") long merchantId);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsListByCode")
	List<CfgGoods> getGoodsListByCode(@Param("start")int page,@Param("pageSize")int pageSize,@Param("goodsCode") String goodsCode);

	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getGoodsList")
	List<CfgGoods> getGoodsList(Map<String, Object> map);
	
	@SelectProvider(type = CfgGoodsSQLBuilder.class, method = "getCount")
	long getCount(Map<String, Object> map);

	@InsertProvider(type = CfgGoodsSQLBuilder.class, method = "batchInsert")
	long batchInsert(ArrayList<ArrayList<Object>> rowlist);
	
	@InsertProvider(type = CfgGoodsSQLBuilder.class, method = "batchInsertByMerchant")
	long batchInsertByMerchant(Map<String, Object> map);
}
