package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CmsBannerSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CmsBannerMapper {

	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getBannerList")
	List<CmsBanner> getBannerList(int ispublished);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getAllBannerList")
	List<CmsBanner> getAllBannerList(Map<String,String> map);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getAllBannerListCount")
	int getAllBannerListCount(String title);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getBannerById")
	CmsBanner getBannerById(String banner_id);
	
	@InsertProvider(type = CmsBannerSQLBuilder.class, method = "insert")
	void insert(CmsBanner cmsBanner);
	
	@UpdateProvider(type = CmsBannerSQLBuilder.class, method = "update")
	void update(CmsBanner cmsBanner);
	
	@DeleteProvider(type = CmsBannerSQLBuilder.class, method = "delete")
	void delete(String bannerId);
}
