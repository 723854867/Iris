package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

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
				VALUES("goods_id", 			"#{goodsId}");
				VALUES("count", 			"#{count}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String batchInsert(StrictMap<List<MemGoodsStore>> map) {
		List<MemGoodsStore> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into "+Table.MEM_GOODS_STORE.mark()+" (merchant_id,goods_id,count,created,updated) values ");
		for(MemGoodsStore store:list){
			stringBuilder.append("('"+store.getMerchantId()+"','"+store.getGoodsId()+"','"+store.getCount()+"','"+store.getCreated()+"','"+store.getUpdated()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_GOODS_STORE.mark());
				SET("store=#{store}");
				SET("updated=#{updated}");
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("goods_id=#{goodsId}");
			}
		}.toString();
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
}
