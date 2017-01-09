package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Role;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface RoleService extends BaseService<Role, Long> {
	public List findPidByRoleId(Long rid);

	List<Role> queryRoles(Map<String,Object> params);

/*	Integer queryRoleCount(Map<String,Object> params);*/

	int updateRole(Role role);

}
