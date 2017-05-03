package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.CzLog;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CzLogSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface CzLogMapper {
	
	@Options(useGeneratedKeys=true,keyColumn="id",keyProperty="id")
	@InsertProvider(type = CzLogSQLBuilder.class , method = "insert")
	void insert(CzLog czLog);
	
	@SelectProvider(type = CzLogSQLBuilder.class , method = "findByCzId")
	CzLog findByCzId(@Param("czId")String czId);

}
