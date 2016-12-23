package org.Iris.app.jilu.storage.mybatis.mapper;

import org.Iris.app.jilu.storage.domain.Relation;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.RelationSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

public interface RelationMapper {
	
	@InsertProvider(type = RelationSQLBuilder.class, method = "insert")
	void insert(Relation relation);

	@SelectProvider(type = RelationSQLBuilder.class, method = "getById")
	Relation getById(String id);
}
