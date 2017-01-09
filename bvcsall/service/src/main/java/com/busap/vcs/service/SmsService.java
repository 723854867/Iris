package com.busap.vcs.service;

import java.util.List;

import com.busap.vcs.data.entity.Sms;

/**
 * Created by
 * User: konglinghai
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface SmsService extends BaseService<Sms, Long> { 
	 
	/**
	 * 	查询手机号
	 * @param uid 用户ID
	 * @param communal 是否包括公共标签
	 * @return
	 */
	public List<Sms> findMySmss(Long uid,boolean communal);
}
