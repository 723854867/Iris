package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.BgVersion;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BgVersionSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BgVersionMapper {
	@Options(useGeneratedKeys = true, keyColumn = "version_id", keyProperty = "versionId")
	@InsertProvider(type = BgVersionSQLBuilder.class, method = "insert")
	void insert(BgVersion cmsVersion);
	@UpdateProvider(type = BgVersionSQLBuilder.class, method = "update")
	void update(BgVersion cmsVersion);
	/**
	 * 获取最新版本
	 * @return
	 */
	@SelectProvider(type = BgVersionSQLBuilder.class, method = "recentVersion")
	BgVersion recentVersion();
	/**
	 * 获取所有版本
	 */
	@SelectProvider(type = BgVersionSQLBuilder.class, method = "getVersions")
	List<BgVersion> getVersions();
	/**
	 * 删除
	 */
	@UpdateProvider(type = BgVersionSQLBuilder.class, method = "delete")
	void delete(@Param("updated") int updated,@Param("versionId") long versionId);
}
