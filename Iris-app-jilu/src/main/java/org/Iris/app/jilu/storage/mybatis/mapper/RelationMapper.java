package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.PubRelation;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.RelationSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface RelationMapper {
	
	@InsertProvider(type = RelationSQLBuilder.class, method = "insert")
	void insert(PubRelation relation);

	@SelectProvider(type = RelationSQLBuilder.class, method = "getById")
	PubRelation getById(String id);
	
	@SelectProvider(type = RelationSQLBuilder.class, method = "count")
	long count(@Param("merchantId") long merchantId);
	
	@SelectProvider(type = RelationSQLBuilder.class, method = "getPager")
	List<PubRelation> getPager(@Param("merchantId") long merchantId, @Param("start") long start, @Param("pageSize") int pageSize);
	
	@DeleteProvider(type = RelationSQLBuilder.class, method = "delete")
	int delete(String id);
}
