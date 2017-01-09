package com.busap.vcs.service;

import com.busap.vcs.data.entity.OrgSettlement;

/**
 * Created by
 * User: zx
 * Date: 28/12/15
 * Time: 11:52 AM
 */
public interface OrgSettlementService extends BaseService<OrgSettlement, Long> {

	public String doOrgSettlement(Long orgId);
	
}
