package org.Iris.igt.template;

import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class IOSTemplateDemo extends AbstractTemplateDemo{

	public IOSTemplateDemo(String appId, String appKey) {
		super(appId, appKey);
	}

	@Override
	public TransmissionTemplate getTemplateDemo(String title, String text) {
		 TransmissionTemplate template = new TransmissionTemplate();
		    template.setAppId(appId);
		    template.setAppkey(appKey);
		    template.setTransmissionContent("透传内容");
		    template.setTransmissionType(2);
		    APNPayload payload = new APNPayload();
		    payload.setBadge(1);
		    payload.setContentAvailable(1);
		    payload.setSound("default");
		    payload.setCategory("$由客户端定义");
		    //简单模式APNPayload.SimpleMsg 
		    payload.setAlertMsg(new APNPayload.SimpleAlertMsg("hello"));
		    //字典模式使用下者
		    //payload.setAlertMsg(getDictionaryAlertMsg());
		    template.setAPNInfo(payload);
		    return template;
	}
}
