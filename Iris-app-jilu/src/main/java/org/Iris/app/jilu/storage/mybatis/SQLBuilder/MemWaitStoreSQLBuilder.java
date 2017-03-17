package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemWaitStore;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

public class MemWaitStoreSQLBuilder {

	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_WAIT_STORE.mark());
				VALUES("order_id", 			"#{orderId}"); 
				VALUES("merchant_id", 		"#{merchantId}"); 
				VALUES("merchant_name", 	"#{merchantName}");
				VALUES("goods_id", 			"#{goodsId}");
				VALUES("goods_name", 		"#{goodsName}");
				VALUES("count", 			"#{count}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_WAIT_STORE.mark());
				SET("count=#{count}");
				SET("updated=#{updated}");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String batchInsert(StrictMap<List<MemWaitStore>> map) {
		List<MemWaitStore> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		stringBuilder.append("insert into "+Table.MEM_WAIT_STORE.mark()+" (order_id,goods_id,goods_name,merchant_id,merchant_name,count,created,updated) values ");
		for(MemWaitStore store:list){
			stringBuilder.append("('"+store.getOrderId()+"','"+store.getGoodsId()+"','"+store.getGoodsName()+"'"
								+ ",'"+store.getMerchantId()+"','"+store.getMerchantName()+"'"
								+ ",'"+store.getCount()+"','"+store.getCreated()+"','"+store.getUpdated()+"'),");
		}
		stringBuilder.setLength(stringBuilder.length() - 1);
		return stringBuilder.toString();
	}
	
	public String batchUpdate(StrictMap<List<MemWaitStore>> map){
		List<MemWaitStore> list = map.get("collection");
		StringBuilder stringBuilder = new StringBuilder(256);
		StringBuilder stringBuilder2 = new StringBuilder(256);
		stringBuilder.append("update "+Table.MEM_WAIT_STORE.mark()+" set count = case id ");
		for(MemWaitStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getCount());
		}
		stringBuilder.append(" end, updated = case id");
		for(MemWaitStore store:list){
			stringBuilder.append(" when "+store.getId()+" then "+store.getUpdated());
			stringBuilder2.append(store.getId()+",");
		}
		stringBuilder.append(" end");
		stringBuilder.append(" where id in(");
		stringBuilder.append(stringBuilder2.substring(0, stringBuilder2.length()-1));
		stringBuilder.append(")");
		return stringBuilder.toString();
	}
	
	public String find(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_WAIT_STORE.mark());
				WHERE("order_id = #{orderId}");
				AND();
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("goods_id = #{goodsId}");
			}
		}.toString();
	}
}
