package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class OrderSQLBuilder {

	public String getOrderById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.ORDER.mark());
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.ORDER.mark());
				VALUES("order_id", 			"#{orderId}");
				VALUES("receive_id", 		"#{receiveId}");
				VALUES("parent_order_id", 	"#{parentOrderId}");
				VALUES("not", 				"#{not}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
}
