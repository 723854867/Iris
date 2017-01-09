package com.busap.vcs.data.repository;

import javax.annotation.Resource;

import com.busap.vcs.data.entity.Sms;

/**
 * Created by djyin on 7/19/2014.
 */
@Resource(name = " Repository")
public interface SmsRepository extends BaseRepository<Sms, Long> {
	
}
