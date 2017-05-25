package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;

public class SysUserSQLBulider {

	public String userAdd() {
		return new SQL() {
			{
				INSERT_INTO("sys_user");
				VALUES("login_no", "#{loginNo}");
				VALUES("user_name", "#{userName}");
				VALUES("password", "#{password}");
			}
		}.toString();
	}
	public String update()
	{
		return new SQL() {
			{
				UPDATE("sys_user");
				SET("password=#{password}");
				SET("last_login_time=#{lastLoginTime}");
				SET("last_login_ip=#{lastLoginIp}");
				WHERE("admin_id=#{adminId}");
			}
		}.toString();
	}
	public String getUserById()
	{
		return "select * from sys_user where user_id=#{userId} and super_flag=0";
	}
	public String getUserByLoginNo()
	{
		return "select * from sys_user where login_no=#{loginNo} and super_flag=0";
	}
	public String getUserList() {

		return "select * from sys_user where 1=1 and super_flag=0 limit #{stratIndex},#{pageSize}";

	}
	public String getCount() {
		return "select count(*) from sys_user where super_flag=0";
	}
	/**
	 * 删除用户
	 */
	public String deleteUser(){
		return "update "+Table.SYS_USER.mark()+" set super_flag=1 where admin_id=#{adminId}";
	}
}
