package com.busap.vcs.data.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.busap.vcs.data.entity.Banner;


/**
 * @author Administrator
 *
 */
public interface BannerDAO {

	/**
	 * 根据showAble查询所有banner(0:默认，1：隐藏，2：全部)
	 * @param params
	 * @return
	 */
	public List<Banner> findAllBanner(@Param("showAble")Integer showAble);

	int updateSort(Map<String,Object> params);

	Banner selectBannerByOrderNum(Map<String,Object> params);

	Banner selectByPrimaryKey(Long id);

	List<Banner> selectBannerList(Map<String,Object> params);

}
