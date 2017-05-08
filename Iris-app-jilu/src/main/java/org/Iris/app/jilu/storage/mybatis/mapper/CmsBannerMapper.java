package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.CmsBanner;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CmsBannerSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CmsBannerMapper {

	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getBannerList")
	List<CmsBanner> getBannerList();
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getGdBannerList")
	List<CmsBanner> getGdBannerList(long count);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getAllBannerList")
	List<CmsBanner> getAllBannerList(Map<String,String> map);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getAllBannerListCount")
	int getAllBannerListCount(String title);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getBannerById")
	CmsBanner getBannerById(long banner_id);
	
	@Options(useGeneratedKeys = true , keyColumn ="banner_id",keyProperty="bannerId")
	@InsertProvider(type = CmsBannerSQLBuilder.class, method = "insert")
	void insert(CmsBanner cmsBanner);
	
	@UpdateProvider(type = CmsBannerSQLBuilder.class, method = "update")
	void update(CmsBanner cmsBanner);
	
	@DeleteProvider(type = CmsBannerSQLBuilder.class, method = "delete")
	void delete(long bannerId);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getPublishBannerList")
	List<CmsBanner> getPublishBannerList(@Param("start") int start,@Param("pageSize") int pageSize);
	
	@SelectProvider(type = CmsBannerSQLBuilder.class, method = "getPublishBannerCount")
	long getPublishBannerCount();
}
