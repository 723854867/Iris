package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Settlement;

/**
 * Created by dmsong on 3/24/2016.
 */
@Resource(name = "settlementRepository")
public interface SettlementRepository extends BaseRepository<Settlement,Long> {

}
