package org.Iris.igt.template;

public abstract class AbstractTemplateDemo implements TemplateDemo<Object>{

	protected String appId;
	protected String appKey;
	
	public AbstractTemplateDemo(String appId,String appKey) {
		this.appId = appId;
		this.appKey = appKey;
	}
}
