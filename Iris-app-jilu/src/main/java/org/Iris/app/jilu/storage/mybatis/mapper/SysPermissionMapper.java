package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.SysPermission;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysPermissionSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SysPermissionMapper {
	@SelectProvider(type = SysPermissionSQLBuilder.class, method = "getByRoleId")
	List<SysPermission> getByRoleId(@Param("roleId")int roleId);
	@SelectProvider(type = SysPermissionSQLBuilder.class, method = "getByUserId")
	List<SysPermission> getByUserId(@Param("userId")int userId);
	/**
	 * 获取所有权限
	 */
	@SelectProvider(type = SysPermissionSQLBuilder.class, method = "getAllPers")
	List<SysPermission> getAllPers();
	/**
	 * 添加权限
	 * @return
	 */
	@Options(keyColumn="permission_id", useGeneratedKeys=true, keyProperty="permissionId")
	@InsertProvider(type = SysPermissionSQLBuilder.class, method = "permissionAdd")
	int permissionAdd(SysPermission sysPermission);
	/**
	 * 权限修改
	 */
	@UpdateProvider(type = SysPermissionSQLBuilder.class, method = "permissionUpdate")
	int permissionUpdate(@Param("permissionId")int permissionId,
			@Param("permissionName")String permissionName,
			@Param("href")String href);
	/**
	 * 权限删除
	 */
	@UpdateProvider(type = SysPermissionSQLBuilder.class, method = "permissionDelete")
	int permissionDelete(@Param("permissionId")int permissionId);
	/**
	 * 通过id查询权限
	 */
	@SelectProvider(type = SysPermissionSQLBuilder.class, method = "getById")
	SysPermission getById(@Param("permissionId")int permissionId);
}
