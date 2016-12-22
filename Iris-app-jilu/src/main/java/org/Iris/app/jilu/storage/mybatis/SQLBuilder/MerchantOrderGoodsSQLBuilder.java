package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MerchantOrderGoods;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MerchantOrderGoodsSQLBuilder {

	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MERCHANT_ORDER_GOODS.mark());
				VALUES("order_id", 		    "#{orderId}");
				VALUES("packet_id", 		"#{packetId}");
				VALUES("goods_id", 			"#{goodsId}");
				VALUES("goods_name", 		"#{goodsName}");
				VALUES("goods_image", 		"#{goodsImage}");
				VALUES("count", 			"#{count}");
				VALUES("unit_price", 		"#{unitPrice}");
				VALUES("status", 			"#{status}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String batchInsert(Map<String, List<MerchantOrderGoods>> map) {
		List<MerchantOrderGoods> list = map.get("list");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into "+Table.MERCHANT_ORDER_GOODS.mark()+" (order_id,packet_id,goods_id,goods_name,goods_image,count,unit_price,status,created,updated) values ");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append("('"+orderGoods.getOrderId()+"','"+orderGoods.getPacketId()+"','"+orderGoods.getGoodsId()+"','"+orderGoods.getGoodsName()+"'"
								+ ",'"+orderGoods.getGoodsImage()+"','"+orderGoods.getCount()+"','"+orderGoods.getUnitPrice()+"'"
								+ ",'"+orderGoods.getStatus()+"','"+orderGoods.getCreated()+"','"+orderGoods.getUpdated()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MERCHANT_ORDER_GOODS.mark());
				SET("packet_id=#{packetId}");
				SET("goods_id=#{goods_id}");
				SET("goods_name=#{goods_name}");
				SET("goods_image=#{goods_image}");
				SET("count=#{count}");
				SET("unit_price=#{unit_price}");
				SET("status=#{status}");
				SET("updated=#{updated}");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String batchUpdate(Map<String, List<MerchantOrderGoods>> map){
		List<MerchantOrderGoods> list = map.get("list");
		StringBuilder stringBuilder = new StringBuilder(256);
		StringBuilder stringBuilder2 = new StringBuilder(256);
		stringBuilder.append("update "+Table.MERCHANT_ORDER_GOODS.mark()+" set packet_id = case id ");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getPacketId());
		}
		stringBuilder.append(" end, goods_id = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getGoodsId());
		}
		stringBuilder.append(" end, goods_name = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getGoodsName());
		}
		stringBuilder.append(" end, goods_image = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getGoodsImage());
		}
		stringBuilder.append(" end, count = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getCount());
		}
		stringBuilder.append(" end, unit_price = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getUnitPrice());
		}
		stringBuilder.append(" end, status = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getStatus());
		}
		stringBuilder.append(" end, updated = case id");
		for(MerchantOrderGoods orderGoods:list){
			stringBuilder.append(" when "+orderGoods.getId()+" then "+orderGoods.getUpdated());
			stringBuilder2.append(orderGoods.getId()+",");
		}
		stringBuilder.append(" end");
		stringBuilder.append(" where id in(");
		stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length()-1));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.MERCHANT_ORDER_GOODS.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String batchDelete(Map<String, List<Long>> map){
		List<Long> list = map.get("list");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("delete from "+Table.MERCHANT_ORDER_GOODS.mark()+" where id in(");
		for(Long id : list){
			stringBuilder.append(id+",");
		}
		
		stringBuilder.setLength(stringBuilder.length() - 1);
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	public String getMemOrderGoodsById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER_GOODS.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}
	

}
