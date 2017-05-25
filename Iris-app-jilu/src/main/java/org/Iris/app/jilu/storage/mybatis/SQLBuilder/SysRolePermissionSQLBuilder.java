package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.SysRolePermission;
import org.Iris.app.jilu.storage.mybatis.Table;
import org.apache.ibatis.jdbc.SQL;


public class SysRolePermissionSQLBuilder {

	public String getList(){
		return new SQL(){
			{
				SELECT("*");
				FROM(Table.SYS_ROLE_PERMISSION.mark());
				WHERE("role_id=#{roleId}");
			}
		}.toString();
	}
	/**
	 * 批量添加
	 */
	public String batchSave(Map map){
		List<SysRolePermission> list = (List<SysRolePermission>) map.get("list");
		int count=1;
		StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO "+Table.SYS_ROLE_PERMISSION.mark()+" (permission_id,role_id) VALUES ");
		for(SysRolePermission srp:list){
			if(count==1)
				builder.append("("+srp.getPermissionId()+","+srp.getRoleId()+")");
			else 
				builder.append(",("+srp.getPermissionId()+","+srp.getRoleId()+")");
			count++;	
		}
     return builder.toString();
		
	}
	/**
	 * 批量删除
	 */
	public String batchDelete(Map map){
		List<Integer> rolePermissions = (List<Integer>) map.get("list");
		int count=1;
		StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM "+Table.SYS_ROLE_PERMISSION.mark()+" WHERE id IN (");
		for(int id:rolePermissions){
			if(count==1)
				builder.append(id);
			else
				builder.append(","+id);
			count++;
		}
		builder.append(")");
		return builder.toString();
	}
	/**
	 * 关联删除
	 */
	public String unionDelete(){
		return "DELETE t1 FROM sys_role_permission t1 LEFT JOIN sys_permission t2 ON t1.permission_id=t2.permission_id WHERE t2.permission_id=#{permissionId} or t2.pid=#{permissionId}";
	}
}
