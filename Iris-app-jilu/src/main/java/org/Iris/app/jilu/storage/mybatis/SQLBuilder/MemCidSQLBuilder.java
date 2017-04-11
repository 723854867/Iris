package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemCidSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_CID.mark());
				VALUES("merchant_id", "#{merchantId}");
				VALUES("client_id", "#{clientId}");
				VALUES("type", "#{type}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
				
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_CID.mark());
				SET("client_id=#{clientId}");
				SET("type=#{type}");
				SET("updated=#{updated}");
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String getMemCid(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_CID.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.MEM_CID.mark());
				WHERE("merchant_id=#{merchantId}");
			}
		}.toString();
	}
}
