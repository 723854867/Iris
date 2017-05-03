package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class CmsAnnoSQLBuilder {

	public String insert() {
		return new SQL() {
			{
				INSERT_INTO(Table.CMS_ANNO.mark());
				VALUES("title", "#{title}");
				VALUES("content", "#{content}");
				VALUES("author", "#{author}");
				VALUES("source", "#{source}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
				VALUES("img", "#{img}");
			}
		}.toString();
	}

	public String update() {
		return new SQL() {
			{
				UPDATE(Table.CMS_ANNO.mark());
				SET("title=#{title}");
				SET("content=#{content}");
				SET("source=#{source}");
				SET("author=#{author}");
				SET("img=#{img}");
				WHERE("anno_id=#{annoId}");
			}
		}.toString();
	}

	public String delete()
	{
		return "UPDATE "+Table.CMS_ANNO.mark()+" set isdel=1 WHERE anno_id=#{annoId}";
	}

	public String getAnnoList() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.CMS_ANNO.mark());
				WHERE("isdel=#{isdel}");
			}
		}.toString();
	}

	public String getAnnoById() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.CMS_ANNO.mark());
				WHERE("anno_Id=#{annoId}");
			}
		}.toString();
	}

	public String getAnno() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.CMS_ANNO.mark());
				WHERE("anno_id=#{anno_id}");
			}
		}.toString();
	}

	public String getCount() {
		return new SQL() {
			{
				SELECT("COUNT(*)");
				FROM(Table.CMS_ANNO.mark());
			}
		}.toString();
	}

	public String getAllAnnoList(Map<String,String> map) {
		String title=map.get("title");
		String startIndex=map.get("startIndex");
		String pageSize=map.get("pageSize");
		
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append("select * from " + Table.CMS_ANNO.mark());
		sqlApp.append(" where isdel=0");
		if(!title.equals(""))
		{
			sqlApp.append(" and title like '%"+title+"%'");
		}
		sqlApp.append(" LIMIT "+startIndex+","+pageSize);
		return sqlApp.toString();
	}
}
