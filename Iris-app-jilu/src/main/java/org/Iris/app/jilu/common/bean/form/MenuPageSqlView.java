package org.Iris.app.jilu.common.bean.form;

public class MenuPageSqlView {
private int menuid;
private int parentmenuid;
private String cname;
private int pageid;
private String url;
public int getMenuid() {
	return menuid;
}
public void setMenuid(int menuid) {
	this.menuid = menuid;
}
public int getParentmenuid() {
	return parentmenuid;
}
public void setParentmenuid(int parentmenuid) {
	this.parentmenuid = parentmenuid;
}
public String getCname() {
	return cname;
}
public void setCname(String cname) {
	this.cname = cname;
}
public int getPageid() {
	return pageid;
}
public void setPageid(int pageid) {
	this.pageid = pageid;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}

}
