package org.Iris.app.jilu.storage.mybatis.mapper;

import java.util.List;

import org.Iris.app.jilu.common.bean.form.MenuPageSqlView;
import org.Iris.app.jilu.storage.domain.SysPage;
import org.Iris.app.jilu.storage.mybatis.SQLBuilder.SysMenuSQLBulider;
import org.apache.ibatis.annotations.SelectProvider;

public interface SysMenuMapper {

	@SelectProvider(type= SysMenuSQLBulider.class, method = "getSysMenuList")
	List<MenuPageSqlView> getSysMenuList();
	
	@SelectProvider(type= SysMenuSQLBulider.class, method = "getParentPageId")
	int getParentPageId(String pagepath);
	
	@SelectProvider(type= SysMenuSQLBulider.class, method = "getPageByPageId")
	SysPage getPageByPageId(int pageid);
	
	@SelectProvider(type= SysMenuSQLBulider.class, method = "getPageByPagePath")
	SysPage getPageByPagePath(String pagepath);
}
