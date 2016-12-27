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
	
	public String update() {
		return new SQL() {
			{
				UPDATE(Table.MEM_CUSTOMER.mark());
				SET("name = #{name}");
				SET("name_prefix_letter = #{namePrefixLetter}");
				SET("mobile = #{mobile}");
				SET("address = #{address}");
				SET("memo = #{memo}");
				SET("updated = #{updated}");
				WHERE("customer_id = #{customerId}");
			}
		}.toString();
	}
	
	public String getMerchantCustomerById() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_CUSTOMER.mark());
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("customer_id = #{customerId}");
			}
		}.toString();
	}
	
	public String getMerchantCustomers() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_CUSTOMER.mark());
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
	
	public String getCustomersByIds(StrictMap<Set<Tuple>> map) {
		Set<Tuple> set = map.get("collection");
		StringBuilder builder = new StringBuilder("SELECT * FROM " + Table.MEM_CUSTOMER.mark() + " WHERE customer_id IN(");
		for (Tuple tuple : set)
			builder.append(tuple.getElement()).append(",");
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		return builder.toString();
	}
}
