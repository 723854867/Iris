package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class StockGoodsStoreLogSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.LOG_STOCK_STORE.mark());
				VALUES("goods_id", 		 "#{goodsId}");
				VALUES("goods_name", 	 "#{goodsName}");
				VALUES("merchant_id",    "#{merchantId}");
				VALUES("merchant_name",  "#{merchantName}");
				VALUES("count",	   		 "#{count}");
				VALUES("price",	   		 "#{price}");
				VALUES("memo",    		 "#{memo}");
				VALUES("created", 		 "#{created}");
			}
		}.toString();
	}
	
	public String getLogList(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.LOG_STOCK_STORE.mark());
				WHERE("goods_id=#{goodsId}");
				AND();
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getLogListByGoodsId(){
		return "select * from "+Table.LOG_STOCK_STORE.mark()+" where goods_id=#{goodsId} order by created desc limit 0,20";
	}
}
