package org.Iris.igt.template;

import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.template.TransmissionTemplate;
/**
 * 透传消息 ios 安卓公用
 * @author 樊水东
 * 2017年4月6日
 */
public class TransmissionTemplateDemo extends AbstractTemplateDemo {

	public TransmissionTemplateDemo(String appId, String appKey) {
		super(appId, appKey);
	}

	@Override
	public TransmissionTemplate getTemplateDemo(String title,String content, String text) {
		TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    template.setTransmissionContent(text);
	    template.setTransmissionType(2);
	    APNPayload payload = new APNPayload();
	    payload.setAutoBadge("+1");
		payload.setSound("default");
		payload.setCategory("$由客户端定义");
		APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
		alertMsg.setBody(content);
		alertMsg.setTitle(title);
		payload.setAlertMsg(alertMsg);
		template.setAPNInfo(payload);
	    return template;
	}
	
}
