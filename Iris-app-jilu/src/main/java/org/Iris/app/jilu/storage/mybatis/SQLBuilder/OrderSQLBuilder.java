package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class OrderSQLBuilder {

	public String getByOrderId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.ORDER_BASEINFO.mark());
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.ORDER_BASEINFO.mark());
				VALUES("order_id", 			"#{orderId}");
				VALUES("receive_id", 		"#{receiveId}");
				VALUES("parent_order_id", 	"#{parentOrderId}");
				VALUES("note", 				"#{note}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
}
