package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.Map;

import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class CmsBannerSQLBuilder {
	public String getBannerList(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.CMS_BANNER.mark());
				WHERE("ispublished=#{ispublished}");
			}
		}.toString();
	}
	
	public String getAllBannerList(Map<String,String> map){
		String title=map.get("title");
		String startIndex=map.get("startIndex");
		String pageSize=map.get("pageSize");
		
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append(this.getAllBannerListSql(title));
		sqlApp.append(" LIMIT "+startIndex+","+pageSize);
		return sqlApp.toString();
	}
	
	public String getAllBannerListCount(String title)
	{
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append("select count(0) from ( ");
		sqlApp.append(this.getAllBannerListSql(title));
		sqlApp.append(" ) zf");
		return sqlApp.toString();
	}
	
	private String getAllBannerListSql(String title)
	{
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append("select * from " + Table.CMS_BANNER.mark());
		sqlApp.append(" where 1=1 ");
		if(!title.equals(""))
		{
			sqlApp.append(" and title like '%"+title+"%'");
		}
		sqlApp.append(" ORDER BY created DESC");
		return sqlApp.toString();
	}
	public String getBannerById() {
		return new SQL() {
			{
				SELECT("*");
				FROM(Table.CMS_BANNER.mark());
				WHERE("banner_id=#{bannerId}");
			}
		}.toString();
	}
	public String insert(CmsBanner cmsBanner)
	{
		return new SQL() {
			{
				INSERT_INTO(Table.CMS_BANNER.mark());
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
				VALUES("bannertype", "#{bannertype}");
				VALUES("title", "#{title}");
				VALUES("summary", "#{summary}");
				VALUES("imgurl", "#{imgurl}");
				VALUES("href", "#{href}");
				VALUES("ispublished", "#{ispublished}");
				VALUES("pushcount", "#{pushcount}");
				VALUES("ispush", "#{ispush}");
			}
		}.toString();
	}
	public String update(CmsBanner cmsBanner)
	{
		return new SQL() {
			{
				UPDATE(Table.CMS_BANNER.mark());
				SET("created=#{created}");
				SET("updated=#{updated}");
				SET("bannertype=#{bannertype}");
				SET("title=#{title}");
				SET("summary=#{summary}");
				SET("imgurl=#{imgurl}");
				SET("href=#{href}");
				SET("ispublished=#{ispublished}");
				SET("ispush=#{ispush}");
				SET("pushcount=#{pushcount}");
				WHERE("banner_id=#{bannerId}");
			}
		}.toString();
	}
	
	public String delete()
	{
		return new SQL() {
			{
				DELETE_FROM(Table.CMS_BANNER.mark());
				WHERE("banner_id=#{bannerId}");
			}
		}.toString();
	}
}
