package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemMerchantSQLBuilder {
	
	public String getById() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_USER.mark());
				WHERE("uid=${uid}");
			}
		}.toString();
	}
}
