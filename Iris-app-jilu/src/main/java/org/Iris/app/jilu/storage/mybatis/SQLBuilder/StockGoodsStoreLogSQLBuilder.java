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
				VALUES("stock_time",	 "#{stockTime}");
				VALUES("memo",    		 "#{memo}");
				VALUES("created", 		 "#{created}");
			}
		}.toString();
	}
	
	public String getLogList(){
		return "select * from "+Table.LOG_STOCK_STORE.mark()+" where goods_id=#{goodsId} and merchant_id =#{merchantId}  order by created desc limit #{start},#{pageSize}";
	}
	
}
