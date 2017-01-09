package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.InviteInfo;

/**
 * Created by lj on 6/17/2015.
 */
@Resource(name = "inviteInfoRepository")
public interface InviteInfoRepository extends BaseRepository<InviteInfo, Long>  {

}
