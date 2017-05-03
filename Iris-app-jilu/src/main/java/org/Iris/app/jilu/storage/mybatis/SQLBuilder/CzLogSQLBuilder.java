package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class CzLogSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.LOG_CZ.mark());
				VALUES("cz_id", 		"#{czId}");
				VALUES("merchant_id",   "#{merchantId}");
				VALUES("cz_time", 		"#{czTime}");
				VALUES("product", 		"#{product}");
				VALUES("money",   		"#{money}");
				VALUES("jb", 	  		"#{jb}");
				VALUES("created", 		"#{created}");
			}
		}.toString();
	}
	
	public String findByCzId(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.LOG_CZ.mark());
				WHERE("cz_id=#{czId}");
			}
		}.toString();
	}
}
