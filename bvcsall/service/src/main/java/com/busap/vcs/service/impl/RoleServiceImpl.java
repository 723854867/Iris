package com.busap.vcs.service.impl;
import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Role;
import com.busap.vcs.data.mapper.RoleDao;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.RoleRepository;
import com.busap.vcs.service.RoleService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 2:44 PM
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<Role, Long> implements
        RoleService {
    @Resource(name = "roleRepository")
    private RoleRepository roleRepository;

    @Resource
    private RoleDao roleDao;

    @Resource(name = "roleRepository")
    @Override
    public void setBaseRepository(BaseRepository<Role, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public void save(Role role) {
        super.save(role);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public Role update(Role role) {
        return (Role) super.update(role);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public Role update(Role role, String... ignoreProperties) {
        return (Role) super.update(role, ignoreProperties);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Transactional
    @CacheEvict(value = {"authorization","userRolePermission"}, allEntries = true)
    public void delete(Role role) {
        super.delete(role);
    }
    
    public List findPidByRoleId(Long rid){
    	return this.roleRepository.findPidByRoleId(rid);
    }

    @Override
    public List<Role> queryRoles(Map<String,Object> params){
        return roleDao.select(params);
    }

/*    @Override
    public Integer queryRoleCount(Map<String,Object> params){
        return roleDao.selectCount(params);
    }*/

    @Override
    public int updateRole(Role role){
        return roleDao.update(role);
    }

}

