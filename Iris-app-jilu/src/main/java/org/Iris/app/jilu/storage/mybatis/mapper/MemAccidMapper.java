package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemAccid;
import org.Iris.app.jilu.storage.domain.MemCid;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemAccidSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemAccidMapper {

	@InsertProvider(type = MemAccidSQLBuilder.class , method = "insert")
	void insert(MemAccid memAccid);
	@UpdateProvider(type = MemAccidSQLBuilder.class , method = "update")
	void update(MemAccid memAccid);
	@SelectProvider(type = MemAccidSQLBuilder.class , method = "getMemAccid")
	MemAccid getMemAccid(long merchantId);
}
