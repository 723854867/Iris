package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

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
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
}
