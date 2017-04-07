package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.MemGoodsStore;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemGoodsStoreSQLBuilder {

	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_GOODS_STORE.mark());
				VALUES("merchant_id", 		"#{merchantId}");
				VALUES("merchant_name", 	"#{merchantName}");
				VALUES("goods_id", 			"#{goodsId}");
				VALUES("goods_code", 		"#{goodsCode}");
				VALUES("goods_name", 		"#{goodsName}");
				VALUES("price", 			"#{price}");
				VALUES("memo", 				"#{memo}");
				VALUES("count", 			"#{count}");
				VALUES("name_prefix_letter","#{namePrefixLetter}");
				VALUES("last_stock_time",	"#{lastStockTime}");
				VALUES("wait_count", 		"#{waitCount}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String batchInsert(StrictMap<List<MemGoodsStore>> map) {
		List<MemGoodsStore> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into "+Table.MEM_GOODS_STORE.mark()+" (merchant_id,merchant_name,goods_id,goods_code,goods_name,count,name_prefix_letter,wait_count,price,memo,created,updated) values ");
		for(MemGoodsStore store:list){
			stringBuilder.append("('"+store.getMerchantId()+"','"+store.getMerchantName()+"','"+store.getGoodsId()+"','"+store.getGoodsCode()+"','"+store.getGoodsName()
			+"','"+store.getCount()+"','"+store.getNamePrefixLetter()+"','"+store.getWaitCount()+"','"+store.getPrice()+"','"+store.getMemo()+"','"+store.getCreated()+"','"+store.getUpdated()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_GOODS_STORE.mark());
				SET("price=#{price}");
				SET("memo=#{memo}");
				SET("count=#{count}");
				SET("wait_count=#{waitCount}");
				SET("sell_count=#{sellCount}");
				SET("last_stock_time=#{lastStockTime}");
				SET("updated=#{updated}");
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
	}
	
	public String batchUpdate(StrictMap<List<MemGoodsStore>> map){
		List<MemGoodsStore> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		StringBuilder stringBuilder2 = new StringBuilder(256);
		stringBuilder.append("update "+Table.MEM_GOODS_STORE.mark()+" set count = case id ");
		for(MemGoodsStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getCount());
		}
		stringBuilder.append(" end, wait_count = case id");
		for(MemGoodsStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getWaitCount());
		}
		stringBuilder.append(" end, sell_count = case id");
		for(MemGoodsStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getSellCount());
		}
		stringBuilder.append(" end, updated = case id");
		for(MemGoodsStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getUpdated());
			stringBuilder2.append(store.getId()+",");
		}
		stringBuilder.append(" end");
		stringBuilder.append(" where id in(");
		stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length()-1));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	public String getMemGoodsStoreById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_GOODS_STORE.mark());
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
	}
	
	public String getCountBymerchantId(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.MEM_GOODS_STORE.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getCountByName(Map<String, Object> para){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.MEM_GOODS_STORE.mark());
				WHERE("goods_name like '%"+para.get("goodsName")+"%'");
			}
		}.toString();
	}
	
	public String getCountByCode(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.MEM_GOODS_STORE.mark());
				WHERE("goods_code=#{goodsCode}");
			}
		}.toString();
	}
	
	public String getMemGoodsStoreList(Map<String, Object> para){
		return "select * from "+Table.MEM_GOODS_STORE.mark()+" "
				+ "where merchant_id=#{merchantId} order by "+para.get("orderByColumn")+" "+para.get("sort")+" LIMIT #{start},#{pageSize}";
	}
	
	public String getMemGoodsStoreListByName(Map<String, Object> para){
		return "select * from "+Table.MEM_GOODS_STORE.mark()+" where goods_name like '%"+para.get("goodsName")+"%' LIMIT #{start},#{pageSize}";
	}
	
	public String getMemGoodsStoreListByCode(){
		return "select * from "+Table.MEM_GOODS_STORE.mark()+" where goods_code = #{goodsCode} LIMIT #{start},#{pageSize}";
	}
}
