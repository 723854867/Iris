package com.busap.vcs.data.repository;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.jpa.repository.Query;

import com.busap.vcs.data.entity.Permission;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "permissionRepository")
public interface PermissionRepository extends BaseRepository<Permission, Long> {
	
	public List<Permission> findByPid(Long pid);
	
//	@Query("select Max(sort) from Wpermission where pid=?1")
//	public Integer getMaxSortByPid(Long pid);
}
