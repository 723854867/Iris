package com.busap.vcs.data.mapper;

import com.busap.vcs.data.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by busap on 2016/3/22.
 */
@Repository("organizationDao")
public interface OrganizationDao {

    int deleteByPrimaryKey(Long id);

    int insert(Organization record);

    Organization selectByPrimaryKey(Long id);

    List<Organization> selectAll(Map<String,Object> params);

    int updateByPrimaryKey(Organization record);

    int updateAnchorCount(Long id);

}
