package com.busap.vcs.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.busap.vcs.data.entity.Complain;

/**
 * Created by
 * User: djyin
 * Date: 12/5/13
 * Time: 11:52 AM
 */
public interface ComplainService extends BaseService<Complain, Long> {
 
	/**
	 * 投诉信息分页查询，dmsong于20150115添加
	 * @param page
	 * @param size
	 * @param filters
	 * @param sort
	 * @return
	 */
	public Page listpage(int page,int size,Map<String,Object> params);
}
