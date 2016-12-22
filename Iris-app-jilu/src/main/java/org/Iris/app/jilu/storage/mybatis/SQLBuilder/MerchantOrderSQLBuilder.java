package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MerchantOrderSQLBuilder {

	public String getOrderById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MERCHANT_ORDER.mark());
				WHERE("order_id=#{orderId}");
				AND();
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MERCHANT_ORDER.mark());
				VALUES("order_id", 				"#{orderId}");
				VALUES("root_order_id", 		"#{rootOrderId}");
				VALUES("super_order_id", 	    "#{superOrderId}");
				VALUES("merchant_id", 			"#{merchantId}");
				VALUES("merchant_name", 		"#{merchantName}");
				VALUES("merchant_address", 		"#{merchantAddress}");
				VALUES("customer_id", 			"#{customerId}");
				VALUES("customer_name", 		"#{customerName}");
				VALUES("customer_mobile", 		"#{customerMobile}");
				VALUES("customer_address", 		"#{customerAddress}");
				VALUES("status", 				"#{status}");
				VALUES("created", 				"#{created}");
				VALUES("updated", 				"#{updated}");
				VALUES("deleted", 				"#{deleted}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MERCHANT_ORDER.mark());
				SET("status=#{status}");
				SET("updated=#{updated}");
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
	public String getMerchantOrderCountGroupByCustomerBetweenTime() {
		return new SQL() {
			{
				SELECT("customer_id, COUNT(*) count");
				FROM(Table.MERCHANT_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
				GROUP_BY("customer_id");
			}
		}.toString();
	}
}
