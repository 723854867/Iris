package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class MemLabelBindSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.MEM_LABEL_BIND.mark());
				VALUES("label_id", "#{labelId}");
				VALUES("merchant_id", "#{merchantId}");
				VALUES("buy_id", "#{buyId}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.MEM_LABEL_BIND.mark());
				SET("status=#{status}");
				SET("bind_id=#{bindId}");
				SET("bind_type=#{bindType}");
				SET("updated=#{updated}");
				WHERE("label_id=#{labelId}");
			}
		}.toString();
	}
	
	public String findById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.MEM_LABEL_BIND.mark());
				WHERE("label_id=#{labelId}");
			}
		}.toString();
	}
	
}
