package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class BgVersionSQLBuilder {
	/**
	 * 新增
	 * @return
	 */
	public String insert() {
		return new SQL() {
			{
				INSERT_INTO(Table.BG_VERSION.mark());
				VALUES("version_num", "#{versionNum}");
				VALUES("status", "#{status}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	/**
	 * 修改
	 */
	public String update(){
		return new SQL(){
			{
				UPDATE(Table.BG_VERSION.mark());
				SET("version_num=#{versionNum}");
				SET("status=#{status}");
				SET("updated=#{updated}");
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
				UPDATE(Table.BG_VERSION.mark());
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
				FROM(Table.BG_VERSION.mark());
				WHERE("del_flag=0");
			}
		}.toString();
	}
	/**
	 * 获取最新版本
	 */
	public String recentVersion(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.BG_VERSION.mark());
				WHERE("created=(SELECT MAX(created) from cms_version where del_flag=0)");
				WHERE("del_flag=0");
			}
		}.toString();
	}
}
