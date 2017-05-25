package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.SysRolePermission;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysRolePermissionSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SysRolePermissionMapper {

	@SelectProvider(type= SysRolePermissionSQLBuilder.class, method = "getList")
	List<SysRolePermission> getList(int roleId);
	/**
	 * 批量添加
	 * @param list
	 * @return
	 */
	@UpdateProvider(type= SysRolePermissionSQLBuilder.class, method = "batchSave")
    int batchSave(List<SysRolePermission> list);
	/**
	 * 批量删除
	 * @param rolePermissions
	 * @return
	 */
	@UpdateProvider(type= SysRolePermissionSQLBuilder.class, method = "batchDelete")
    int batchDelete(List<Integer> rolePermissions);
	/**
	 * 删除权限时关联删除
	 */
	@DeleteProvider(type= SysRolePermissionSQLBuilder.class, method = "unionDelete")
	int unionDelete(int permissionId);
}
