package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.SysRole;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysRoleSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SysRoleMapper {
	@Options(useGeneratedKeys = true, keyColumn = "role_id", keyProperty = "roleId")
	@InsertProvider(type = SysRoleSQLBuilder.class, method = "insert")
	int insert(SysRole sysRole);
	@SelectProvider(type= SysRoleSQLBuilder.class, method = "getByUserId")
	@Results({
		@Result(id = true, column = "role_id", property = "roleId"), 
        @Result(property = "sysPermissions", column = "role_id",many = @Many(select = "org.zimo.app.qydj.mybatis.mapper.SysPermissionMapper.getByRoleId"))
	})
	List<SysRole> getByUserId(@Param("userId")int userId);
	//获取所有角色
	@SelectProvider(type= SysRoleSQLBuilder.class, method = "getRoles")
	List<SysRole> getRoles(@Param("pageStart")int pageStart, @Param("pageSize")int pageSize);
	@SelectProvider(type= SysRoleSQLBuilder.class, method = "getRolesCount")
	public int getRolesCount();
	/**
	 * 权限修改
	 */
	@UpdateProvider(type= SysRoleSQLBuilder.class, method = "roleUpdate")
	public int roleUpdate(@Param("roleId")int roleId, @Param("roleName") String roleName);
	
	//获取所有角色
	@SelectProvider(type= SysRoleSQLBuilder.class, method = "getAllRoles")
	List<SysRole> getAllRoles();
	
	@DeleteProvider(type= SysRoleSQLBuilder.class, method = "delete")
	void delete(long roleId);
}
