package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemJbDetail;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemJbDetailSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface MemJbDetailMapper {

	@InsertProvider(type = MemJbDetailSQLBuilder.class, method = "insert")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	void insert(MemJbDetail memJbDetail);
	
	@SelectProvider(type = MemJbDetailSQLBuilder.class, method = "getJbDetail")
	List<MemJbDetail> getJbDetail(@Param("merchantId")long merchantId,@Param("start")int start,@Param("pageSize")int pageSize);
	
	@SelectProvider(type = MemJbDetailSQLBuilder.class, method = "getJbDetailCount")
	long getJbDetailCount(long merchantId);
	
	@SelectProvider(type = MemJbDetailSQLBuilder.class, method = "getRevenue")
	long getRevenue(long merchantId);
	
	@SelectProvider(type = MemJbDetailSQLBuilder.class, method = "getExpenses")
	long getExpenses(long merchantId);
}
