package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.SensitiveWord;

@Resource(name = "sensitiveWordRepository")
public interface SensitiveWordRepository extends BaseRepository<SensitiveWord, Long> {
	public SensitiveWord findById(String Id);
}
