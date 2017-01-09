package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Forward;

@Resource(name = "forwardRepository")
public interface ForwardRepository extends BaseRepository<Forward, Long> {
	
}
