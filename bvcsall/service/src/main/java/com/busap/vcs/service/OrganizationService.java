package com.busap.vcs.service;

import com.busap.vcs.data.entity.Organization;

import java.util.List;
import java.util.Map;

/**
 * Created by huoshanwei on 2016/3/22.
 */
public interface OrganizationService {

    int deleteByPrimaryKey(Long id);

    int insert(Organization record);

    Organization queryOrganization(Long id);

    List<Organization> queryAll(Map<String,Object> params);

    int updateByPrimaryKey(Organization record);

    int updateAnchorCount(Long id);

}
