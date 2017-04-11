package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemCid;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemCidSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemCidMapper {

	@InsertProvider(type = MemCidSQLBuilder.class , method = "insert")
	void insert(MemCid memCid);
	@UpdateProvider(type = MemCidSQLBuilder.class , method = "update")
	void update(MemCid memCid);
	@SelectProvider(type = MemCidSQLBuilder.class , method = "getMemCid")
	MemCid getMemCid(long merchantId);
	@DeleteProvider(type = MemCidSQLBuilder.class , method = "delete")
	void delete(long merchantId);
}
