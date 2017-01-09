package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Organization;
import com.busap.vcs.data.mapper.OrganizationDao;
import com.busap.vcs.service.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2016/3/22.
 */
@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationDao organizationDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return organizationDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Organization record) {
        return organizationDao.insert(record);
    }

    @Override
    public Organization queryOrganization(Long id) {
        return organizationDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Organization> queryAll(Map<String,Object> params) {
        return organizationDao.selectAll(params);
    }

    @Override
    public int updateByPrimaryKey(Organization record) {
        return organizationDao.updateByPrimaryKey(record);
    }

    @Override
    public int updateAnchorCount(Long id){
        return organizationDao.updateAnchorCount(id);
    }

}
