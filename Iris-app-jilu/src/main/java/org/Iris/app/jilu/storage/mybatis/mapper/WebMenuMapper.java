package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.MenuPageSqlView;
import org.Iris.app.jilu.storage.domain.SysPage;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.WebMenuSQLBulider;
import org.apache.ibatis.annotations.SelectProvider;

public interface WebMenuMapper {

	@SelectProvider(type= WebMenuSQLBulider.class, method = "getSysMenuList")
	List<MenuPageSqlView> getSysMenuList();
	
	@SelectProvider(type= WebMenuSQLBulider.class, method = "getParentPageId")
	int getParentPageId(String pagepath);
	
	@SelectProvider(type= WebMenuSQLBulider.class, method = "getPageByPageId")
	SysPage getPageByPageId(int pageid);
	
	@SelectProvider(type= WebMenuSQLBulider.class, method = "getPageByPagePath")
	SysPage getPageByPagePath(String pagepath);
}
