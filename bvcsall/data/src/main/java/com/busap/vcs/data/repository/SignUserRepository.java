package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.SignUser;

/**
 * Created by lj on 7/20/2015.
 */
@Resource(name = "signUserRepository")
public interface SignUserRepository extends BaseRepository<SignUser,Long> {

}
