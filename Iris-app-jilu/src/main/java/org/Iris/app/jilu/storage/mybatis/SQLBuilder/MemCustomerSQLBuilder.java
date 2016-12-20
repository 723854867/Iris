package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemCustomerSQLBuilder {

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_CUSTOMER.mark());
				VALUES("merchant_id", 		"#{merchantId}");
				VALUES("name", 				"#{name}");
				VALUES("address", 			"#{address}");
				VALUES("mobile", 			"#{mobile}");
				VALUES("memo", 				"#{memo}");
				VALUES("ID_Number", 		"#{IDNumber}");
				VALUES("name_prefix_letter","#{namePrefixLetter}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String getMerchantCustomers() {
		return new SQL() {
			{
				SELECT("customer_id, name_prefix_letter, last_purchase_time, purchase_sum");
				FROM(Table.MEM_CUSTOMER.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getCustomersByIds(List<Long> list) {
		StringBuilder builder = new StringBuilder("SELECT * FROM mem_customer WHERE customer_id IN(");
		for (Long id : list)
			builder.append(id).append(",");
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}
}
