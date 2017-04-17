package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class BgUserSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.BG_USER.mark());
				VALUES("account", "#{account}");
				VALUES("password", "#{password}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.BG_USER.mark());
				SET("password=#{password}");
				SET("last_login_time=#{lastLoginTime}");
				SET("updated=#{updated}");
				WHERE("account=#{account}");
			}
		}.toString();
	}
	
	public String find(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.BG_USER.mark());
				WHERE("account=#{account}");
			}
		}.toString();
	}
}
