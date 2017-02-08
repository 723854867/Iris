package org.Iris.igt.template;

import com.gexin.rp.sdk.template.LinkTemplate;

public class LinkTemplateDemo extends AbstractTemplateDemo{

	public LinkTemplateDemo(String appId,String appKey) {
		super(appId,appKey);
	}
	
	@Override
	public LinkTemplate getTemplateDemo(String title, String text) {
		 LinkTemplate template = new LinkTemplate();
		    // 设置APPID与APPKEY
		    template.setAppId(appId);
		    template.setAppkey(appKey);
		    // 设置通知栏标题与内容
		    template.setTitle(title);
		    template.setText(text);
		    // 配置通知栏图标
		    template.setLogo("icon.png");
		    // 配置通知栏网络图标
		    template.setLogoUrl("");
		    // 设置通知是否响铃，震动，或者可清除
		    template.setIsRing(true);
		    template.setIsVibrate(true);
		    template.setIsClearable(true);
		    // 设置打开的网址地址
		    template.setUrl("http://www.getui.com");
		    // 设置定时展示时间
		    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		    return template;
	}
}
