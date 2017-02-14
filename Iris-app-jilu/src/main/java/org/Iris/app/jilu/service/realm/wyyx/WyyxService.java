package org.Iris.app.jilu.service.realm.wyyx;

import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.WyyxCreateAccountResultForm;
import org.Iris.app.jilu.common.http.ApiUri;
import org.Iris.app.jilu.common.http.HttpClientUtil;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.network.http.handler.SyncJsonRespHandler;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 网易云信服务类 主要用于生成 商户对应的云信账号与token
 * 
 * @author 樊水东 2017年2月9日
 */
@Service
public class WyyxService {

	/**
	 * 获取云信id和token
	 */
	Logger logger = LoggerFactory.getLogger(WyyxService.class);

	private String appKey;
	private String appSecret;
	private String nonce;
	private String curTime;
	private String checkSum;

	public void init() {
		this.appKey = "709be4058503ae62a09172f7584e8602";
		this.appSecret = "8a2311977a0b";
		this.nonce = "12345";
		this.curTime = String.valueOf((System.currentTimeMillis() / 1000L));
		this.checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);
	}

	public WyyxCreateAccountResultForm createWyyxIdAndToken(String accid) {
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.CREATE_YXID);
		setHeader(httpPost);

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("accid", accid));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			return Beans.httpProxy.syncRequest(httpPost,
					SyncJsonRespHandler.<WyyxCreateAccountResultForm> build(WyyxCreateAccountResultForm.class));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.WYYX_ACCOUNT_CREATE_FAIL);
		}
	}
	
	public WyyxCreateAccountResultForm updateWyyxIdAndToken(String accid) {
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.UPDATE_YXID);
		setHeader(httpPost);

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("accid", accid));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			return Beans.httpProxy.syncRequest(httpPost,
					SyncJsonRespHandler.<WyyxCreateAccountResultForm> build(WyyxCreateAccountResultForm.class));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.WYYX_ACCOUNT_CREATE_FAIL);
		}
	}
	
	public WyyxCreateAccountResultForm refreshWyyxIdAndToken(String accid) {
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.REFRESH_YXID);
		setHeader(httpPost);

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("accid", accid));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			// 执行请求
			return Beans.httpProxy.syncRequest(httpPost,
					SyncJsonRespHandler.<WyyxCreateAccountResultForm> build(WyyxCreateAccountResultForm.class));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.WYYX_ACCOUNT_CREATE_FAIL);
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

	public static void main(String[] args) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// String url = "https://api.netease.im/nimserver/user/create.action";
		HttpPost httpPost = HttpClientUtil.getPost(ApiUri.CREATE_YXID);
		// 设置请求的header
		String appKey = "709be4058503ae62a09172f7584e8602";
		String appSecret = "8a2311977a0b";
		String nonce = "12345";
		String curTime = String.valueOf((System.currentTimeMillis() / 1000L));
		String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);
		httpPost.addHeader("AppKey", appKey);
		httpPost.addHeader("Nonce", nonce);
		httpPost.addHeader("CurTime", curTime);
		httpPost.addHeader("CheckSum", checkSum);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

		// 设置请求的参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("accid", "29_"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			 // 执行请求
	        HttpResponse response = httpClient.execute(httpPost);

	        // 打印执行结果
	        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
		} catch (Exception e) {
			throw IllegalConstException.errorException(JiLuCode.ACCOUNT_ALREADY_BINDED);
		}
	}
}
