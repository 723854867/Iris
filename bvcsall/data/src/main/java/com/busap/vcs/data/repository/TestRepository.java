package com.busap.vcs.data.repository;

import com.busap.vcs.data.entity.Test;

import javax.annotation.Resource;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = "testRepository")
public interface TestRepository extends BaseRepository<Test, Long> {
}
