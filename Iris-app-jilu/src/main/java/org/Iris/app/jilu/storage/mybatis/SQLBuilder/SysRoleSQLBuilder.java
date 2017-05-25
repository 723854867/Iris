package org.Iris.app.jilu.storage.mybatis.SQLBuilder;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.Iris.util.lang.DateUtils;
import org.apache.ibatis.jdbc.SQL;

public class SysRoleSQLBuilder {
	public String insert(){
		return new SQL(){
			{
				INSERT_INTO(Table.SYS_ROLE.mark());
				VALUES("role_name", "#{roleName}");
				VALUES("created", "#{created}");
				VALUES("updated", "#{updated}");
			}
		}.toString();
	}
	/**
	 * 查询用户的角色
	 */
	public String getByUserId(){
		return new SQL(){
			{
				SELECT("sr.*");
				FROM(Table.SYS_ROLE.mark()+" sr");
				LEFT_OUTER_JOIN(Table.SYS_USER_ROLE.mark()+" su on sr.role_id = su.role_id");
				WHERE("su.user_id=#{userId}");
			}
		}.toString();
	}
	public String getRoles(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.SYS_ROLE.mark());
			}
		}.toString()+" limit #{pageStart},#{pageSize}";
	}
	
	public String getAllRoles(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.SYS_ROLE.mark());
			}
		}.toString();
	}
	
	public String getRolesCount(){
		return new SQL(){
			{
				SELECT("count(*)");
				FROM(Table.SYS_ROLE.mark());
			}
		}.toString();
	}
	/**
	 * 角色修改
	 */
	public String roleUpdate(){
		return new SQL(){
			{
				UPDATE(Table.SYS_ROLE.mark());
				SET("role_name=#{roleName}");
				SET("updated="+DateUtils.currentTime());
				WHERE("role_id=#{roleId}");
			}
		}.toString();
	}
	
	public String delete(){
		return new SQL(){
			{
				DELETE_FROM(Table.SYS_ROLE.mark());
				WHERE("role_id=#{roleId}");
			}
		}.toString();
	}
}
