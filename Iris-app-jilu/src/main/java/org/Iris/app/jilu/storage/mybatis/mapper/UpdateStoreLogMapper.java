package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.UpdateStoreLog;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.UpdateStoreLogSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;

public interface UpdateStoreLogMapper {

	@InsertProvider(type = UpdateStoreLogSQLBuilder.class , method = "insert")
	void insert(UpdateStoreLog log);
}
