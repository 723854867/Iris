package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.BgConfig;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BgConfigSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BgConfigMapper {

	@InsertProvider(type = BgConfigSQLBuilder.class, method = "insert")
	void insert(BgConfig bgConfig);
	
	@UpdateProvider(type = BgConfigSQLBuilder.class, method = "update")
	void update(@Param("key")String key, @Param("value")String value);
	
	@SelectProvider(type = BgConfigSQLBuilder.class, method = "list")
	List<BgConfig> list();
	
	@SelectProvider(type = BgConfigSQLBuilder.class, method = "findByKey")
	String findByKey(String key);
}
