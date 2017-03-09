package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemOrderGoods;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemOrderGoodsSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemOrderGoodsMapper {

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MemOrderGoodsSQLBuilder.class, method = "insert")
	void insert(MemOrderGoods order);

	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchInsert")
	void batchInsert(List<MemOrderGoods> list);

	@UpdateProvider(type = MemOrderGoodsSQLBuilder.class, method = "update")
	void update(MemOrderGoods order);

	@UpdateProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchUpdate")
	void batchUpdate(List<MemOrderGoods> list);

	@DeleteProvider(type = MemOrderGoodsSQLBuilder.class, method = "delete")
	void delete(long id);

	@DeleteProvider(type = MemOrderGoodsSQLBuilder.class, method = "batchDelete")
	void batchDelete(List<MemOrderGoods> list);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsById")
	MemOrderGoods getMerchantOrderGoodsById(long id);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsByOrderId")
	MemOrderGoods getMerchantOrderGoodsByOrderId(@Param("orderId") String orderId,@Param("goodsId") long goodsId);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getMerchantOrderGoodsByList")
	List<MemOrderGoods> getMerchantOrderGoodsByList(List<MemOrderGoods> list);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getChangeMerchantOrderGoodsByOrderId")
	List<MemOrderGoods> getChangeMerchantOrderGoodsByOrderId(String orderId);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getNotFinishMerchantOrderGoodsByOrderId")
	List<MemOrderGoods> getNotFinishMerchantOrderGoodsByOrderId(String orderId);
	
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getPacketMerchantOrderGoodsByPacketId")
	List<MemOrderGoods> getPacketMerchantOrderGoodsByPacketId(String packetId);
	//找到父订单单个产品未转传出去的部分
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getSuperNotChangeOrderGoods")
	MemOrderGoods getSuperNotChangeOrderGoods(@Param("orderId") String orderId,@Param("goodsId") long goodsId);
	//找到父订单单个产品转出去的部分
	@SelectProvider(type = MemOrderGoodsSQLBuilder.class, method = "getSuperChangeOrderGoods")
	MemOrderGoods getSuperChangeOrderGoods(@Param("orderId") String orderId,@Param("goodsId") long goodsId,@Param("count") int count);
}
