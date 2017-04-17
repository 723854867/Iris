package org.Iris.app.jilu.service.realm.wyyx;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.http.ApiUri;
import org.Iris.app.jilu.common.http.HttpClientUtil;
import org.Iris.app.jilu.service.realm.wyyx.result.SendSmsResult;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.network.http.handler.SyncJsonRespHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

/**
 * 云信短信服务
 * @author 樊水东
 * 2017年3月9日
 */
@Service
public class SmsService {
	
	private String appKey;
	private String appSecret;
	private String nonce;
	private String curTime;
	private String checkSum;

	public void init() {
		this.appKey = "2e5c412341b19ed7aad2bf8e065393ff";
		this.appSecret = "ab2b09551e3b";
		this.nonce = "12345";
		this.curTime = String.valueOf((System.currentTimeMillis() / 1000L));
		this.checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);
	}

	/**
	 * 发送验证码
	 * @param mobile
	 * @param templateid 短信模板id 
	 * @param codeLen 短信位数
	 * @return
	 */
	public SendSmsResult sendSms(String mobile,int templateid,int codeLen){
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.SEND_SMS);
		setHeader(httpPost);

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobile));
		nvps.add(new BasicNameValuePair("templateid", String.valueOf(templateid)));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			return Beans.httpProxy.syncRequest(httpPost,
					SyncJsonRespHandler.<SendSmsResult> build(SendSmsResult.class));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.API_INVOKE_ERROR);
		}
	}
	/**
	 * 短信验证
	 * @param mobile
	 * @param code
	 * @return
	 */
	public SendSmsResult validateSms(String mobile,String code){
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.SMS_VALIDATE);
		setHeader(httpPost);
		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobile));
		nvps.add(new BasicNameValuePair("code", code));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			return Beans.httpProxy.syncRequest(httpPost,
					SyncJsonRespHandler.<SendSmsResult> build(SendSmsResult.class));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.API_INVOKE_ERROR);
		}
	}
	
	private void setHeader(HttpPost httpPost) {
		// 设置请求的header
		httpPost.addHeader("AppKey", appKey);
		httpPost.addHeader("Nonce", nonce);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	}
}
