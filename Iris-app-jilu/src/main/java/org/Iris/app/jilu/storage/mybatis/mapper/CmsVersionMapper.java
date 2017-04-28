package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.CmsVersion;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CmsVersionSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CmsVersionMapper {
	@Options(useGeneratedKeys = true, keyColumn = "version_id", keyProperty = "versionId")
	@InsertProvider(type = CmsVersionSQLBuilder.class, method = "insert")
	void insert(CmsVersion cmsVersion);
	@UpdateProvider(type = CmsVersionSQLBuilder.class, method = "update")
	void update(CmsVersion cmsVersion);
	/**
	 * 获取最新版本
	 * @return
	 */
	@SelectProvider(type = CmsVersionSQLBuilder.class, method = "recentVersion")
	CmsVersion recentVersion(@Param("operatSys") int operatSys);
	/**
	 * 获取所有版本
	 */
	@SelectProvider(type = CmsVersionSQLBuilder.class, method = "getVersions")
	List<CmsVersion> getVersions(@Param("pageIndex")int pageIndex,@Param("pageSize") int pageSize);
	/**
	 * 删除
	 */
	@UpdateProvider(type = CmsVersionSQLBuilder.class, method = "delete")
	void delete(@Param("updated") int updated,@Param("versionId") long versionId);
}
