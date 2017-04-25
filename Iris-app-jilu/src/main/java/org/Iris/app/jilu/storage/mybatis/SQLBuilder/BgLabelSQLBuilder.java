package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

public class BgLabelSQLBuilder {

	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.BG_LABEL.mark());
				VALUES("label_num", "#{labelNum}");
				VALUES("price", "#{price}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.BG_LABEL.mark());
				SET("label_num=#{labelNum}");
				SET("price=#{price}");
				SET("updated=#{updated}");
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String findById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.BG_LABEL.mark());
				WHERE("id=#{id}");
			}
		}.toString();
	}
	
	public String findByNum(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.BG_LABEL.mark());
				WHERE("label_num=#{labelNum}");
			}
		}.toString();
	}
	
	public String list(){
		return "select * from "+Table.BG_LABEL.mark()+" order by created desc limit #{start},#{pageSize}";
	}
	
	public String count(){
		return new SQL(){
			{
				SELECT("count(1)");
				FROM(Table.BG_LABEL.mark());
			}
		}.toString();
	}
	
	public String delete(){
		return "delete from "+Table.BG_LABEL.mark()+" where id = #{id}";
	}
}
