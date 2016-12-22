package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MerchantAccountSQLBuilder {

	public String getByAccount() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MERCHANT_ACCOUNT.mark());
				WHERE("account=#{account}");
			}
		}.toString();
	}
	
	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MERCHANT_ACCOUNT.mark());
				VALUES("account", 			"#{account}");
				VALUES("type", 				"#{type}");
				VALUES("merchant_id", 		"#{merchantId}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
}
