package org.Iris.app.jilu.service.realm.igt.domain;

import org.Iris.app.jilu.common.bean.enums.IgtPushType;
import org.Iris.app.jilu.storage.domain.CmsBanner;

public class PushBannerParam extends PushCommonParam{

	private String head;
	private String summary;
	private String fmUrl;
	private String href;
	
	public PushBannerParam(CmsBanner banner) {
		title = IgtPushType.BANNER_PUBLISH.getTitle();
		content = IgtPushType.BANNER_PUBLISH.getContent();
		this.head = banner.getTitle();
		this.summary = banner.getSummary();
		this.fmUrl = banner.getFmUrl();
		this.href = banner.getHref();
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	
}
