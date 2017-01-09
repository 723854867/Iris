package com.busap.vcs.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送
 * @version 2014-11-3 15:31:16
 */
@Component
public class NotificationJPushUtil implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(NotificationJPushUtil.class);
	
	private static final long serialVersionUID = -7821968818404317068L;
	
	/**
	 * <p>应用标识（AppKey）这是一个应用最核心标识，唯一地标识一个应用。应用标识需要正确地配置到客户端SDK中，以关联客户端与服务器端。</p>
	 */
	@Value("${appKey}")
	private String appKey;//="c0c6264a01a1cf44952d0048";
	/**
	 * 极光推送主密码
	 * <p>API MasterSecret（主密码）,专用于 API 调用，用来验证 API 调用的合法性。与 AppKey 配合使用。
	 * 请确保此密码的保密。如果外泄，别人将可以以你的身份来调用极光推送 API。
	 * 由于客户端容易被破解，所以我们强烈建议：不要在客户端直接调用极光推送 API，否则你的 API MasterSecret 将容易被窃取。</p>
	 */
	@Value("${masterSecret}")
	private String masterSecret;//="f6919a0a811c01b28dac45ba";//正式; // "2b38ce69b1de2a7fa95706ea";
	
	private enum Type{
		All,
		Message,
		Notification;
	}
	
	/**
	 * 极光推送主接口
	 */
	private static JPushClient client;
	
	/**
	 * 创建推送对象，type 为Notification时，title 和 extra 没有作用
	 * 
	 * @param type 推送类型 （Message 或 Notification）为空则全部推送
	 * @param platform 推送平台<b>默认all</b>
	 * @param content 推送内容 <b>不能为空， notification[长度为58个字符] , message[总长度为234个字符， 是消息内容+【发送给某人的id（3字符个算一个长度）】的和]，超出”极光“会自动截取</b>
	 * @param alias 用户别名来标识一个用户。一个设备只能绑定一个别名，但多个设备可以绑定同一个别名。一次推送最多 1000 个。
	 * @param registrationIDs 极光推送返回的 id。<b>可以为空。 </b><br><code>集成了 JPush SDK 的应用程序在第一次成功注册到 JPush 服务器时，JPush 服务器会给客户端返回一个唯一的该设备的标识 - RegistrationID。
	 * 	JPush SDK 会以广播的形式发送 RegistrationID 到应用程序</code>
	 * @param extra 额外消息
	 * @param options 自定义设置
	 * @return PushResult 推送结果<br>
	 * <p>含有 exception字段异常出现{exception={"msg_id": 1055043469, "error": {"message": "authen failed", "code": 1004}}} ， isok（boolean） 消息是否推送成功， msg_id 消息推送ID，sendno 发送序号</p>
	 * @see http://docs.jpush.io/server/rest_api_v3_push/
	 */
	/*
	 * <pre>
	 * platform		必填	推送平台设置
	 * audience		必填	推送设备指定
	 * notification	可选	通知内容体。是被推送到客户端的内容。与 message 一起二者必须有其一，可以二者并存
	 * message		可选	消息内容体。是被推送到客户端的内容。与 notification 一起二者必须有其一，可以二者并存
	 * options		可选	推送参数
	 * registrationID 推送给指定ID
	 * alias 目前是手机客户端设置 Ruser的id 为某设备的别名
	 * </pre>
	 */
	public Map<String,Object> send(String type,String platform,String content,Map<String,String> extra,String alias,String registrationIDs,Options options){
		Builder build = PushPayload.newBuilder();
		Type sendType=getSendType(type);
		if(sendType==Type.All){
			build.setNotification(buildNotification(content));
			build.setMessage(buildMessage(content,null));
		}else if(sendType==Type.Notification){
			build.setNotification(buildNotification(content));
		}else if(sendType==Type.Message){
			build.setMessage(buildMessage(content, null));
		}
		
		build.setPlatform(getPlatform(platform));
		
		// regist 和 alias 判断
		if(registrationIDs!=null&&!registrationIDs.equals("")&&!registrationIDs.equals("null")){
			build.setAudience(Audience.registrationId(registrationIDs.split(",")));
		}else if(alias!=null&&!alias.equals("")&&!alias.equals("null")){
			build.setAudience(Audience.alias(alias));
		}else{
			build.setAudience(Audience.all());
		}
		
		Map<String,Object> re=new HashMap<String, Object>();
		PushResult result;
		try {
			result = getJPushClient().sendPush(build.build());
			re.put("msg_id", result.msg_id);
			re.put("sendno", result.sendno);
			re.put("isok", result.isResultOK());
		} catch (APIRequestException e) {
			// e.getMessage() = 
			//{exception={"msg_id": 601824843, "error": {"message": "authen failed", "code": 1004}}, isok=false};
			re.put("isok", false);
			re.put("msg_id", e.getMsgId());
			re.put("exception", e.getErrorMessage());
			logger.error("认证失败",e);
		} catch (APIConnectionException e) {
			re.put("isok", false);
			re.put("exception", e.getMessage());
			logger.error("连接失败",e);
		}
		
		return re;
	}
	
	public Map<String,Object> sendMessage(String platform,String content,Map<String,String> extra,String tags){
		Builder build = PushPayload.newBuilder();
		build.setMessage(buildMessage(content,extra));
		
		build.setPlatform(getPlatform(platform));
		
		build.setAudience(Audience.tag(tags));
		Map<String,Object> re=new HashMap<String, Object>();
		PushResult result;
		try {
			result = getJPushClient().sendPush(build.build());
			re.put("msg_id", result.msg_id);
			re.put("sendno", result.sendno);
			re.put("isok", result.isResultOK());
		} catch (APIRequestException e) {
			re.put("isok", false);
			re.put("msg_id", e.getMsgId());
			re.put("exception", e.getErrorMessage());
			logger.error("认证失败",e);
		} catch (APIConnectionException e) {
			re.put("isok", false);
			re.put("exception", e.getMessage());
			logger.error("连接失败",e);
		}
		
		return re;
	}
	
	public Map<String,Object> send(String content){
		return send(null,null, content, null, null, null,null);
	}
	public Map<String,Object> send(String type,String content){
		return send(type,null, content, null, null, null,null);
	}
	public Map<String,Object> send(String type,String platform,String content){
		return send(type, platform, content, null, null, null,null);
	}
	public Map<String,Object> sendByRgistrationID(String content,String registrationIDs){
		return send(null, null, content, null, null,registrationIDs, null);
	}
	public Map<String,Object> send(String type,String platform,String content,Options options){
		return send(type, platform, content, null, null, null,options);
	}
	
	/**
	 * 
	 * @param type
	 * @param platform
	 * @param content
	 * @param alias
	 * @return
	 */
	public Map<String,Object> sendByAlias(String type,String platform,String content,String alias){
		return this.send(type, platform, content, null, alias, null, null);
	}
	
	/**
	 * 发送给某用户
	 * @param type 发送类型 推送类型 （Message 或 Notification）为空则全部推送
	 * @param content 消息内容
	 * @param alias 用户id
	 * @return
	 */
	public Map<String,Object> sendByAlias(String type,String content,String alias){
		return this.send(null, null, content, null, alias, null, null);
	}
	
	
	
	/**
	 * 创建消息
	 * @param title 消息标题
	 * @param content 消息内容
	 * @param extra 额外内容
	 * @return Message
	 * @throws NullPointerException
	 */
	private Message buildMessage(String content,Map<String,String> extra) throws NullPointerException{
		cn.jpush.api.push.model.Message.Builder builder=Message.newBuilder();
		if(content==null||content.equals("")){
			throw new NullPointerException("发送内容不能为 null");
		}
		
		builder.setMsgContent(content);
		if(extra!=null){
			builder.addExtras(extra);
		}
		
		return builder.build();
	}
	
	/**
	 * 创建通知
	 * @param title 通知标题
	 * @param content 通知内容
	 * @param extra 额外内容
	 * @return Message
	 * @throws NullPointerException
	 */
	private Notification buildNotification(String content) throws NullPointerException{
		
		cn.jpush.api.push.model.notification.Notification.Builder builder=Notification.newBuilder();
		if(content==null||content.equals("")){
			throw new NullPointerException("内容不能为 null");
		}
		//builder.addPlatformNotification(AndroidNotification.newBuilder().setTitle("android测试").build());
		builder.setAlert(content);
		return builder.build();	}
	
	public JPushClient getJPushClient() {
		if(client==null){
			return client=new JPushClient(masterSecret, appKey);
		}
		return client;
	}
	
	public String getAppKey() {
		return appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}
	
	/**
	 * 选择平台
	 * @param platform （android, ios） 为空全选
	 * @return
	 */
	private static Platform getPlatform(String platform){
		if(platform==null||platform.equals("")){
			return Platform.all();
		}else if(platform.equalsIgnoreCase("android")){
			return Platform.android();
		}else if(platform.equalsIgnoreCase("ios")){
			return Platform.ios();
		}
		return Platform.all();
	}
	
	/**
	 * 发送类型
	 * @param type （message, notification） 为空或不存在则全选
	 * @return
	 */
	private static Type getSendType(String type){
		if(type==null||type.equals("")){
			return Type.All;
		}else if(type.equalsIgnoreCase("notification")){
			return Type.Notification;
		}else if(type.equalsIgnoreCase("message")){
			return Type.Message;
		}
		return Type.All;
	}

	public static void main(String[] args) {
		NotificationJPushUtil util=new NotificationJPushUtil();
		/*try {
			JPushClient client=util.getJPushClient();
			PushResult result;
			result = client.sendMessageAll("234234");
			System.out.println("getOriginalContent :"+result.getOriginalContent());
			System.out.println("getRateLimitQuota :"+result.getRateLimitQuota());
			System.out.println("getRateLimitRemaining :"+result.getRateLimitRemaining());
			System.out.println("getRateLimitReset :"+result.getRateLimitReset());
			System.out.println("msg_id: "+result.msg_id);
			System.out.println("sendno: "+result.sendno);
			System.out.println("isResultOK :"+result.isResultOK());
		} catch (APIConnectionException e) {
			System.out.println("error 1");
			System.out.println(e.getMessage());
		} catch (APIRequestException e) {
			System.out.println("error 2");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}*/
		
		Map<String,Object> send=util.send("w34r3werse");
		System.out.println(send);
		
	}
}
