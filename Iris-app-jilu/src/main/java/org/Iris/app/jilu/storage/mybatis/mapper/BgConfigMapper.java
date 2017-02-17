package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.BgConfig;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BgConfigSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BgConfigMapper {

	@InsertProvider(type = BgConfigSQLBuilder.class, method = "insert")
	void insert(BgConfig bgConfig);
	
	@UpdateProvider(type = BgConfigSQLBuilder.class, method = "update")
	void update(BgConfig bgConfig);
}
