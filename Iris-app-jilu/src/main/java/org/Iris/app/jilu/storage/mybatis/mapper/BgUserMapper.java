package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.BgUser;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BgUserSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BgUserMapper {

	@InsertProvider(type = BgUserSQLBuilder.class , method = "insert")
	void insert(BgUser bgUser);
	
	@SelectProvider(type = BgUserSQLBuilder.class , method = "find")
	BgUser find(String account);
	
	@UpdateProvider(type = BgUserSQLBuilder.class , method = "update")
	void update(BgUser bgUser);
}
