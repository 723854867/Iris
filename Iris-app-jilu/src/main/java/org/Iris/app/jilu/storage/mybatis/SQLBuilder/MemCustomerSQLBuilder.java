package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Set;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.defaults.DefaultSqlSession.StrictMap;

import redis.clients.jedis.Tuple;

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
	
	public String getMemCustomerById(){
		return "select * from "+Table.MEM_CUSTOMER.mark()+" where customer_id = #{customerId}";
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
	
	public String getCustomersByIds(StrictMap<Set<Tuple>> map) {
		Set<Tuple> set = map.get("collection");
		StringBuilder builder = new StringBuilder("SELECT * FROM mem_customer WHERE customer_id IN(");
		for (Tuple tuple : set)
			builder.append(tuple.getElement()).append(",");
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}
}
