package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemAccidSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_ACCID.mark());
				VALUES("merchant_id", "#{merchantId}");
				VALUES("accid", "#{accid}");
				VALUES("token", "#{token}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
				
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_ACCID.mark());
				SET("accid=#{accid}");
				SET("token=#{token}");
				SET("updated=#{updated}");
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getMemAccid(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_ACCID.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
}
