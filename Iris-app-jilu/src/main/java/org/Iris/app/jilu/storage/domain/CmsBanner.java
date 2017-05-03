package org.Iris.app.jilu.storage.domain;

public class CmsBanner {

	public long bannerId;
	public int created;
	public int updated;
	public int bannertype;
	public String title;
	public String summary;
	public String imgurl;
	public String href;
	public int ispublished;
	public int ispush;
	public int pushcount;
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
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
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
	public int getIspush() {
		return ispush;
	}
	public void setIspush(int ispush) {
		this.ispush = ispush;
	}
	public int getPushcount() {
		return pushcount;
	}
	public void setPushcount(int pushcount) {
		this.pushcount = pushcount;
	}
	
	
}
