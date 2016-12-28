package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemOrderSQLBuilder {

	public String getOrderById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ORDER.mark());
				WHERE("order_id = #{orderId}");
				AND();
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
	
	public String insert(){
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_ORDER.mark());
				VALUES("order_id", 				"#{orderId}");
				VALUES("root_order_id", 		"#{rootOrderId}");
				VALUES("super_order_id", 	    "#{superOrderId}");
				VALUES("super_merchant_id", 	"#{superMerchantId}");
				VALUES("super_merchant_name", 	"#{superMerchantName}");
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
				UPDATE(Table.MEM_ORDER.mark());
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
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("root_order_id = 0");
				GROUP_BY("customer_id");
			}
		}.toString();
	}
	
	public String getChangeMerchantOrderList(){
		return new SQL() {
			{
				SELECT("order_id,super_merchant_id,super_merchant_name");
				FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.MEM_ORDER.mark());
				WHERE("merchant_id=#{merchantId}");
				AND();
				WHERE("order_id=#{orderId}");
			}
		}.toString();
	}
}
