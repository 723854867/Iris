package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class UpdateStoreLogSQLBuilder {

	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.LOG_UPDATE_STORE.mark());
				VALUES("order_id", 			"#{orderId}"); 
				VALUES("merchant_id", 		"#{merchantId}"); 
				VALUES("merchant_name", 	"#{merchantName}");
				VALUES("goods_id", 			"#{goodsId}");
				VALUES("goods_name", 		"#{goodsName}");
				VALUES("old_price", 		"#{oldPrice}");
				VALUES("new_price", 		"#{newPrice}");
				VALUES("old_count", 		"#{oldCount}");
				VALUES("new_count", 		"#{newCount}");
				VALUES("old_memo", 			"#{oldMemo}");
				VALUES("new_memo", 			"#{newMemo}");
				VALUES("operation", 		"#{operation}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
}
