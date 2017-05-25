package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class SysUserRoleSQLBuilder {
	public String userRoleAdd(){
		return new SQL(){
			{
				INSERT_INTO(Table.SYS_USER_ROLE.mark());
				VALUES("user_id", "#{userId}");
				VALUES("role_id", "#{roleId}");
			}
		}.toString();
	}
	/**
	 * 删除用户信息
	 */
	public String userRoleDel(){
		return new SQL(){
			{
				DELETE_FROM(Table.SYS_USER_ROLE.mark());
				WHERE("user_id=#{userId}");
			}
		}.toString();
	}
}
