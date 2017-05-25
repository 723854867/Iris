package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.SysUserRole;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysUserRoleSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface SysUserRoleMapper {
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	@InsertProvider (type= SysUserRoleSQLBuilder.class, method = "userRoleAdd")
	void userRoleAdd(SysUserRole sysUserRole);
	/**
	 * 删除用户信息
	 */
	@DeleteProvider(type = SysUserRoleSQLBuilder.class, method = "userRoleDel")
	int userRoleDel(@Param("userId")int userId);
}
