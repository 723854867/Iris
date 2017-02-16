package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.annotations.Select;
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
				SELECT("count(1)");
				FROM(Table.RELATION.mark());
				WHERE("(acceptor = #{merchantId} OR applier = #{merchantId})");
				AND();
				WHERE("`mod` = 1");
			}
		}.toString();
	}
	
	public String getPager() {
		
		return "select * from "+Table.RELATION.mark()+" where (acceptor = #{merchantId} OR applier = #{merchantId}) and `mod`=1 limit #{start}, #{pageSize}";
	}
	
	public String getPager_(){
		return "select (*) from (select p.acceptor,p.created,a.accid,a.token from pub_relation p left join mem_accid a on p.acceptor = a.merchant_id where p.applier = #{merchantId} and p.mod = 1"
				+"union"
				+"select p.applier,p.created,a.accid,a.token from pub_relation p left join mem_accid a on p.applier = a.merchant_id where p.acceptor = #{merchantId} and p.mod = 1)m "
				+ "order by m.created limit #{start}, #{pageSize}";
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
