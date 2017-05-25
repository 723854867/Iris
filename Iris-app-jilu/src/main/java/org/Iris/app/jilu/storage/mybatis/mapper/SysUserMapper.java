package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.SysUser;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysUserSQLBulider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.transaction.annotation.Transactional;


public interface SysUserMapper {

	@SelectProvider(type= SysUserSQLBulider.class, method = "getUserByLoginNo")
	SysUser getUserByLoginNo(String loginNo);

	@SelectProvider(type= SysUserSQLBulider.class, method = "getUserByLoginNo")
	@Results({
		@Result(id = true, column = "admin_id", property = "adminId"), 
        @Result(property = "sysRoleSet", column = "admin_id",many = @Many(select = "org.zimo.app.qydj.mybatis.mapper.SysRoleMapper.getByUserId"))
	})
	SysUser getUserAndRole(String loginNo);
	@Options(useGeneratedKeys = true, keyColumn = "admin_id", keyProperty = "adminId")
	@InsertProvider (type= SysUserSQLBulider.class, method = "userAdd")
	void userAdd(SysUser sysUser);
	
	@SelectProvider(type= SysUserSQLBulider.class, method = "getUserList")
	List<SysUser> getUserList(@Param("stratIndex")int stratIndex,@Param("pageSize")int pageSize);
	
	@SelectProvider(type= SysUserSQLBulider.class, method = "getCount")
	int getCount();
	
	@UpdateProvider(type= SysUserSQLBulider.class, method = "update")
	int update(SysUser sysUser);
	/**
	 * 删除用户
	 */
	@Transactional
	@UpdateProvider(type= SysUserSQLBulider.class, method = "deleteUser")
	public int deleteUser(@Param("adminId")int adminId);
}
