package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class StockGoodsStoreLogSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.LOG_STOCK_STORE.mark());
				VALUES("goods_id", 		 "#{goodsId}");
				VALUES("merchant_id",    "#{merchantId}");
				VALUES("count",	   		 "#{count}");
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
}
