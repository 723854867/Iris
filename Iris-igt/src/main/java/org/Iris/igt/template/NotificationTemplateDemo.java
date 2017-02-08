package org.Iris.igt.template;

import com.gexin.rp.sdk.template.NotificationTemplate;

public class NotificationTemplateDemo extends AbstractTemplateDemo{
	
	public NotificationTemplateDemo(String appId,String appKey) {
		super(appId,appKey);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public NotificationTemplate getTemplateDemo(String title,String text) {
		 NotificationTemplate template = new NotificationTemplate();
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
		    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		    template.setTransmissionType(2);
		    template.setTransmissionContent("");
		    // 设置定时展示时间
		    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		    return template;
	}
	
}
