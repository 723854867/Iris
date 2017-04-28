package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class CmsVersionSQLBuilder {
	/**
	 * 新增
	 * @return
	 */
	public String insert() {
		return new SQL() {
			{
				INSERT_INTO(Table.CMS_VERSION.mark());
				VALUES("version_num", "#{versionNum}");
				VALUES("status", "#{status}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
				VALUES("content", "#{content}");
				VALUES("download_url", "#{downloadUrl}");
				VALUES("operat_sys", "#{operatSys}");
			}
		}.toString();
	}
	/**
	 * 修改
	 */
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.CMS_VERSION.mark());
				SET("version_num=#{versionNum}");
				SET("status=#{status}");
				SET("updated=#{updated}");
				SET("del_flag=#{delFlag}");
				SET("content=#{content}");
				SET("download_url=#{downloadUrl}");
				WHERE("version_id=#{versionId}");
			}
		}.toString();
	}
	
	/**
	 * 删除
	 */
	public String delete(){
		return new SQL(){
			{
				UPDATE(Table.CMS_VERSION.mark());
				SET("del_flag=1");
				SET("updated=#{updated}");
				WHERE("version_id=#{versionId}");
			}
		}.toString();
	}
	
	/**
	 * 获取所有版本
	 */
	public String getVersions(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.CMS_VERSION.mark());
				WHERE("del_flag=0 ");
			}
		}.toString()+" order by created desc limit #{pageIndex},#{pageSize}";
	}
	/**
	 * 获取最新版本
	 */
	public String recentVersion(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.CMS_VERSION.mark());
				WHERE("created=(SELECT MAX(created) from cms_version where del_flag=0 and operat_sys=#{operatSys})");
				WHERE("operat_sys=#{operatSys}");
				WHERE("del_flag=0");
			}
		}.toString();
	}
}
