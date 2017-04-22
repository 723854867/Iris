package org.Iris.app.jilu.storage.domain;

public class SysPage {
private int pageid;
private int parentpageid;
private String url;
public int getPageid() {
	return pageid;
}
public void setPageid(int pageid) {
	this.pageid = pageid;
}
public int getParentpageid() {
	return parentpageid;
}
public void setParentpageid(int parentpageid) {
	this.parentpageid = parentpageid;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}

}
