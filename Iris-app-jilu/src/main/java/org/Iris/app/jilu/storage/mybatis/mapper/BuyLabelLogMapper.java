package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.BuyLabelLog;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BuyLabelLogSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BuyLabelLogMapper {

	@InsertProvider(type = BuyLabelLogSQLBuilder.class , method = "insert")
	void insert(BuyLabelLog buyLabelLog);
	
	@UpdateProvider(type = BuyLabelLogSQLBuilder.class , method = "update")
	void update(BuyLabelLog buyLabelLog);
	
	@SelectProvider(type = BuyLabelLogSQLBuilder.class , method = "findById")
	BuyLabelLog findById(long id);
	
	@SelectProvider(type = BuyLabelLogSQLBuilder.class , method = "count")
	long count(Map<String, Object> map);
	
	@SelectProvider(type = BuyLabelLogSQLBuilder.class , method = "list")
	List<BuyLabelLog> list(Map<String, Object> map);
}
