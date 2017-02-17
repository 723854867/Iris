package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class BgConfigSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.BG_CONFIG.mark());
				VALUES("`key`", "#{key}");
				VALUES("`value`", "#{value}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.BG_CONFIG.mark());
				SET("`value`=#{value}");
				WHERE("`key`=#{key}");
			}
		}.toString();
	}
}
