package org.Iris.app.jilu.storage.domain;

public class CmsBanner {

	public long bannerId;
	public int created;
	public int updated;
	public int bannertype;
	public String title;
	public String summary;
	public String fmUrl;
	public String gdUrl;
	public int gdType;
	public String href;
	public int ispublished;
	public long getBannerId() {
		return bannerId;
	}
	public void setBannerId(long bannerId) {
		this.bannerId = bannerId;
	}
	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public int getUpdated() {
		return updated;
	}
	public void setUpdated(int updated) {
		this.updated = updated;
	}
	public int getBannertype() {
		return bannertype;
	}
	public void setBannertype(int bannertype) {
		this.bannertype = bannertype;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFmUrl() {
		return fmUrl;
	}
	public void setFmUrl(String fmUrl) {
		this.fmUrl = fmUrl;
	}
	public String getGdUrl() {
		return gdUrl;
	}
	public void setGdUrl(String gdUrl) {
		this.gdUrl = gdUrl;
	}
	public int getGdType() {
		return gdType;
	}
	public void setGdType(int gdType) {
		this.gdType = gdType;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public int getIspublished() {
		return ispublished;
	}
	public void setIspublished(int ispublished) {
		this.ispublished = ispublished;
	}
	
	
}
