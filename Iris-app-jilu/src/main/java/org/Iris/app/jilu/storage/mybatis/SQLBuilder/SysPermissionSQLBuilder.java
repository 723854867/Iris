package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.Iris.util.lang.DateUtils;
import org.apache.ibatis.jdbc.SQL;

public class SysPermissionSQLBuilder {
	public String getByRoleId(){
		return new SQL(){
			{
				SELECT("sp.*");
				FROM(Table.SYS_PERMISSION.mark()+" sp");
				LEFT_OUTER_JOIN(Table.SYS_ROLE_PERMISSION.mark()+" sr on sp.permission_id=sr.permission_id");
				WHERE("sr.role_id = #{roleId}");
				WHERE("sp.del_flag = 0");
			}
		}.toString();
	}
	public String getByUserId(){
		return new SQL(){
			{
				SELECT("sp.*");
				FROM(Table.SYS_PERMISSION.mark()+" sp");
				LEFT_OUTER_JOIN(Table.SYS_ROLE_PERMISSION.mark()+" sr on sp.permission_id=sr.permission_id");
				LEFT_OUTER_JOIN(Table.SYS_USER_ROLE.mark()+" su on su.role_id=sr.role_id");
				WHERE("su.user_id = #{userId}");
				WHERE("sp.del_flag = 0");
			}
		}.toString();
	}
	/**
	 * 获取所有权限
	 */
	public String getAllPers(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.SYS_PERMISSION.mark());
				WHERE("del_flag=0");
			}
		}.toString();
	}
	/**
	 * 权限添加
	 */
	public String permissionAdd(){
		return new SQL(){
			{
				INSERT_INTO(Table.SYS_PERMISSION.mark());
				VALUES("pid", "#{pid}");
				VALUES("permission_name", "#{permissionName}");
				VALUES("href", "#{href}");
				VALUES("type", "#{type}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	/**
	 * 权限修改
	 */
	public String permissionUpdate(){
		return new SQL(){
			{
				UPDATE(Table.SYS_PERMISSION.mark());
				SET("permission_name=#{permissionName}");
				SET("href=#{href}");
				SET("updated="+DateUtils.currentTime());
				WHERE("permission_id=#{permissionId}");
			}
		}.toString();
	}
	/**
	 * 权限标记删除
	 */
	public String permissionDelete(){
		return "update "+Table.SYS_PERMISSION.mark()+ " set del_flag=1, updated="+DateUtils.currentTime()+" where permission_id=#{permissionId} or pid=#{permissionId}";
	}
	/**
	 * 通过id查询
	 */
	public String getById(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.SYS_PERMISSION.mark());
				WHERE("permission_id=#{permissionId}");
			}
		}.toString();
	}
}
