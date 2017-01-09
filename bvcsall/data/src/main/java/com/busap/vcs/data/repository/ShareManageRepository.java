package com.busap.vcs.data.repository;

import com.busap.vcs.data.entity.ShareManage;

import javax.annotation.Resource;

/**
 * Repository
 * Created by Knight on 15/11/16.
 */
@Resource(name = "shareManageRepository")
public interface ShareManageRepository extends BaseRepository<ShareManage, Long> {

}
