package org.Iris.app.jilu.service.realm.weixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.WeiXinAccessTokenResult;
import org.Iris.app.jilu.common.http.ApiUri;
import org.Iris.app.jilu.common.http.HttpClientUtil;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.network.http.handler.SyncJsonRespHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;

/**
 * 微信登陆服务
 * @author 樊水东
 * 2017年2月14日
 */
@Service
public class WeiXinService {

	private String appId;
	private String appSecret;
	
	public void init(){
		this.appId = "";
		this.appSecret = "";
	}
	
	public WeiXinAccessTokenResult getAccessToken(String code){
		HttpPost post = HttpClientUtil.getPost(ApiUri.GET_WEIXIN_LOGIN_TOKEN);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", appId));
		params.add(new BasicNameValuePair("secret", appSecret));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		try {
			return Beans.httpProxy.syncRequest(post, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.GET_WEIXIN_ACCESSTOKEN_FAIL);
		}
	}
	
	public WeiXinAccessTokenResult refreshAccessToken(String refresh_token){
		HttpPost post = HttpClientUtil.getPost(ApiUri.WEIXIN_REFRESH_TOKEN);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appid", appId));
		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
		params.add(new BasicNameValuePair("refresh_token", refresh_token));
		try {
			return Beans.httpProxy.syncRequest(post, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
		}
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
}
