package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.MemLabelBind;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.MemLabelBindSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface MemLabelBindMapper {

	@InsertProvider(type = MemLabelBindSQLBuilder.class , method = "insert")
	void insert(MemLabelBind memLabelBind);
	
	@InsertProvider(type = MemLabelBindSQLBuilder.class , method = "batchInsert")
	void batchInsert(List<MemLabelBind> list);
	
	@UpdateProvider(type = MemLabelBindSQLBuilder.class , method = "update")
	void update(MemLabelBind memLabelBind);
	
	@SelectProvider(type = MemLabelBindSQLBuilder.class , method = "findById")
	MemLabelBind findById(String labelId);
}
