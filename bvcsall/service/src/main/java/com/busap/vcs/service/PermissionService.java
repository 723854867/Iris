package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Permission;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface PermissionService extends BaseService<Permission, Long> {

	List<Permission> findAll(Long pid);
	
//	public Integer getMaxSortByPid(Long pid);
}
