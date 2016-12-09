package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemAccountSQLBuilder {

	public String getByAccount() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ACCOUNT.mark());
				WHERE("account=#{account}");
			}
		}.toString();
	}
}
