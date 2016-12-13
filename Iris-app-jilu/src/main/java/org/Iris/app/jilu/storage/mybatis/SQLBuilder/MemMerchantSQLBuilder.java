package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemMerchantSQLBuilder {
	
	public String getByMerchantId() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_MERCHANT.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_MERCHANT.mark());
				VALUES("name", 				"#{name}");
				VALUES("address", 			"#{address}");
				VALUES("avatar", 			"#{avatar}");
				VALUES("last_login_time", 	"#{lastLoginTime}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_MERCHANT.mark());
				SET("name=#{name}");
				SET("address=#{address}");
				SET("avatar=#{avatar}");
				SET("updated=#{updated}");
				WHERE("merchant_id=#{merchantId}");
				
			}
		}.toString();
	}
}
