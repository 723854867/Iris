package org.Iris.app.jilu.storage.mybatis.SQLBuilder;

public class SysMenuSQLBulider {

	public String getSysMenuList() {

		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append(" SELECT a.`menuid`,a.`parentmenuid`,a.`cname`,a.`pageid`,b.`url` FROM bg_menu a   ");
		sqlApp.append(" LEFT JOIN bg_page b ON b.`pageid`=a.`pageid` ORDER BY a.`menuid` ");
		return sqlApp.toString();

	}
	
	public String getParentPageId()
	{
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append(" SELECT parentpageid FROM bg_page a WHERE a.`url`=#{pagepath}");
		return sqlApp.toString();
	}
	
	public String getPageByPageId()
	{
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append(" SELECT a.* FROM bg_page a WHERE a.`pageid`=#{pageid}");
		return sqlApp.toString();
	}
	
	public String getPageByPagePath()
	{
		StringBuilder sqlApp=new StringBuilder();
		sqlApp.append(" SELECT a.* FROM bg_page a WHERE a.`url`=#{pagepath}");
		return sqlApp.toString();
	}
}