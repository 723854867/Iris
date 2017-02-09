package org.Iris.igt.push;

import java.util.ArrayList;
import java.util.List;

import org.Iris.igt.template.TransmissionTemplateDemo;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.uitls.AppConditions;
import com.gexin.rp.sdk.http.IGtPush;

public class AppPushManager {

	/**
	 * 给单一客户机推送消息
	 * 
	 * @param cid
	 * @param title
	 * @param text
	 */
	private String appId;
	private String appKey;
	private String masterSecret;
	private String url;
	private IGtPush push;
	//private NotificationTemplateDemo notificationTemplateDemo;
	//private IOSTemplateDemo iosTemplateDemo;
	private TransmissionTemplateDemo transmissionTemplateDemo;
	
	public void init(){
		push = new IGtPush(url, appKey, masterSecret);
		//notificationTemplateDemo = new NotificationTemplateDemo(appId,appKey);
		transmissionTemplateDemo = new TransmissionTemplateDemo(appId, appKey);
		//iosTemplateDemo = new IOSTemplateDemo(appId, appKey);
	}
	
	public IPushResult pushToSingle(String cid, String title, String text){
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(transmissionTemplateDemo.getTemplateDemo(title, text));
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);

		Target target = new Target();
		target.setAppId(appId);
		target.setClientId(cid);

		return push.pushMessageToSingle(message, target);
	}

	/**
	 * 给一个列表推送消息
	 * 
	 * @param cids
	 * @param title
	 * @param text
	 */
	public IPushResult pushToList(List<String> cids, String title, String text) {
		ListMessage message = new ListMessage();
		message.setData(transmissionTemplateDemo.getTemplateDemo(title, text));
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		// 配置推送目标
		List<Target> targets = new ArrayList<Target>();
		for (String cid : cids) {
			Target target = new Target();
			target.setAppId(appId);
			target.setClientId(cid);
			targets.add(target);
		}
		// taskId用于在推送时去查找对应的message
		String taskId = push.getContentId(message);
		return push.pushMessageToList(taskId, targets);
	}
	/**
	 * 给应用所有用户推送
	 * @param title
	 * @param text
	 * @return
	 */
	public IPushResult pushToApp(String title, String text) {
		AppMessage message = new AppMessage();
		message.setData(transmissionTemplateDemo.getTemplateDemo(title, text));

		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		// 推送给App的目标用户需要满足的条件
		AppConditions cdt = new AppConditions();
		List<String> appIdList = new ArrayList<String>();
		appIdList.add(appId);
		message.setAppIdList(appIdList);
		// 手机类型
		List<String> phoneTypeList = new ArrayList<String>();
		// 省份
		List<String> provinceList = new ArrayList<String>();
		// 自定义tag
		List<String> tagList = new ArrayList<String>();

		cdt.addCondition(AppConditions.PHONE_TYPE, phoneTypeList);
		cdt.addCondition(AppConditions.REGION, provinceList);
		cdt.addCondition(AppConditions.TAG, tagList);
		message.setConditions(cdt);

		return push.pushMessageToApp(message);
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
