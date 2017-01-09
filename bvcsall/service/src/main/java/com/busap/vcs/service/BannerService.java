package com.busap.vcs.service;

import java.util.List;
import java.util.Map;

import com.busap.vcs.data.entity.Banner;

/**
 * Created by
 * User: dmsong
 * Date: 6/8/15
 * Time: 11:52 AM
 */
public interface BannerService extends BaseService<Banner, Long> {
 
	List<Banner> findAllBanner(Integer showAble);

	int updateSort(Map<String,Object> params);

	Banner queryBannerByOrderNum(Map<String,Object> params);

	boolean updateBannerSort(Long id,Integer type) throws Throwable;

	List<Banner> queryBannerList(Map<String,Object> params);

}
