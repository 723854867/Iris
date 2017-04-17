package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.MemPayInfo;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemPayInfoSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemPayInfoMapper {

	@InsertProvider(type = MemPayInfoSQLBuilder.class , method = "insert")
	void insert(MemPayInfo memPayInfo);
	
	@UpdateProvider(type = MemPayInfoSQLBuilder.class , method = "update")
	void update(MemPayInfo memPayInfo);
	
	@SelectProvider(type = MemPayInfoSQLBuilder.class , method = "findByOutRradeNo")
	MemPayInfo findByOutRradeNo(String outTradeNo);
}
