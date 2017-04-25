package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.storage.domain.BgLabel;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.BgLabelSQLBuilder;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface BgLabelMapper {

	@Options(useGeneratedKeys=true,keyColumn="id",keyProperty="id")
	@InsertProvider(type = BgLabelSQLBuilder.class, method = "insert")
	void insert(BgLabel bgLabel);
	
	@UpdateProvider(type = BgLabelSQLBuilder.class, method = "update")
	void update(BgLabel bgLabel);
	
	@SelectProvider(type = BgLabelSQLBuilder.class, method = "findById")
	BgLabel findById(long id);
	
	@SelectProvider(type = BgLabelSQLBuilder.class, method = "findByNum")
	BgLabel findByNum(String labelNum);
	
	@SelectProvider(type = BgLabelSQLBuilder.class, method = "list")
	List<BgLabel> list(@Param("start")int start,@Param("pageSize")int pageSize);
	
	@SelectProvider(type = BgLabelSQLBuilder.class, method = "count")
	long count();
	
	@DeleteProvider(type = BgLabelSQLBuilder.class, method = "delete")
	void delete(long id);
}
