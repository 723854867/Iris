package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.Iris.app.jilu.storage.domain.CmsAnno;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.CmsAnnoSQLBuilder;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface CmsAnnoMapper {

	@InsertProvider(type = CmsAnnoSQLBuilder.class, method = "insert")
	void insert(CmsAnno cmsAnno);

	@UpdateProvider(type = CmsAnnoSQLBuilder.class, method = "update")
	void update(CmsAnno cmsAnno);
	
	@UpdateProvider (type = CmsAnnoSQLBuilder.class, method = "delete")
	void delete(Long anno_id);

	@SelectProvider(type = CmsAnnoSQLBuilder.class, method = "getAnnoList")
	List<CmsAnno> getAnnoList(int isdel);
	
	@SelectProvider(type = CmsAnnoSQLBuilder.class, method = "getAnno")
	CmsAnno getAnno(String anno_id);

	@SelectProvider(type = CmsAnnoSQLBuilder.class, method = "getAllAnnoList")
	List<CmsAnno> getAllAnnoList(Map<String,String> map);

	@SelectProvider(type = CmsAnnoSQLBuilder.class, method = "getCount")
	int getCount();
	
	@SelectProvider(type = CmsAnnoSQLBuilder.class, method = "getAnnoById")
	List<CmsAnno> getAnnoById(Long annoId);
}
