package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class RelationSQLBuilder {

	public String insert() { 
		return new SQL() {
			{
				INSERT_INTO(Table.RELATION.mark());
				VALUES("id", "#{id}");
				VALUES("applier", "#{applier}");
				VALUES("acceptor", "#{acceptor}");
				VALUES("applier_memo", "#{applierMemo}");
				VALUES("acceptor_memo", "#{acceptorMemo}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String getById() { 
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.RELATION.mark());
				WHERE("id = #{id}");
			}
		}.toString();
	}
	
	public String count() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.RELATION.mark());
				WHERE("(acceptor = #{merchantId} OR applier = #{merchantId})");
				AND();
				WHERE("mod = 1");
			}
		}.toString();
	}
	
	public String getPager() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.RELATION.mark());
				WHERE("(acceptor = #{merchantId} OR applier = #{merchantId})");
				AND();
				WHERE("mod = 1");
				WHERE("limit #{start}, #{pageSize}");
			}
		}.toString();
	}
	
	public String delete() { 
		return new SQL() {
			{
				DELETE_FROM(Table.RELATION.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}
}
