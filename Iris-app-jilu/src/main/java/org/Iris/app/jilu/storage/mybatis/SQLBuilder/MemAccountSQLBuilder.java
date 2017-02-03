package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemAccountSQLBuilder {

	public String getByAccount() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ACCOUNT.mark());
				WHERE("account = #{account}");
			}
		}.toString();
	}
	
	public String getByMerchantIdAndType(){
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ACCOUNT.mark());
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("type = #{type}");
			}
		}.toString();
	}
	
	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.MEM_ACCOUNT.mark());
				VALUES("account", 			"#{account}");
				VALUES("type", 				"#{type}");
				VALUES("merchant_id", 		"#{merchantId}");
				VALUES("created", 			"#{created}");
				VALUES("updated", 			"#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_ACCOUNT.mark());
				SET("account=#{account}");
				SET("updated=#{updated}");
				WHERE("merchant_id = #{merchantId}");
				AND();
				WHERE("type = #{type}");
			}
		}.toString();
	}
	
	public String getByMerchantId(){
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.MEM_ACCOUNT.mark());
				WHERE("merchant_id = #{merchantId}");
			}
		}.toString();
	}
}
