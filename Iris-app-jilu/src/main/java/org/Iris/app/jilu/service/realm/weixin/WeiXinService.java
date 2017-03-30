package org.Iris.app.jilu.service.realm.weixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.http.HttpClientUtil;
import org.Iris.app.jilu.service.realm.weixin.result.WeiXinAccessTokenResult;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.pay.wechat.util.Configure;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.util.network.http.handler.SyncJsonRespHandler;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 微信登陆服务
 * @author 樊水东
 * 2017年2月14日
 */
@Service
public class WeiXinService {

	/**
	 * 获取accessToken
	 * @param code
	 * @return
	 */
	
	private static Logger logger = LoggerFactory.getLogger(WeiXinService.class);
	
	public WeiXinAccessTokenResult getAccessToken(String code){
		List<String> params = new ArrayList<String>();
		params.add("appid="+Configure.getAppid());
		params.add("secret="+Configure.getSecret());
		params.add("code="+code);
		params.add("grant_type=authorization_code");
		HttpGet gHttpGet = new HttpGet(HttpClientUtil.getUrl(Configure.ACCESS_TOKEN_API, params));
		
		try {
			return Beans.httpProxy.syncRequest(gHttpGet, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.GET_WEIXIN_ACCESSTOKEN_FAIL);
		}
	}
	/**
	 * 刷新accessToken
	 * @param refresh_token
	 * @return
	 */
	public WeiXinAccessTokenResult refreshAccessToken(String refresh_token){
		List<String> params = new ArrayList<String>();
		params.add("appid="+Configure.getAppid());
		params.add("grant_type=refresh_token");
		params.add("refresh_token="+refresh_token);
		HttpGet gHttpGet = new HttpGet(HttpClientUtil.getUrl(Configure.REFRESH_ACCESS_TOKEN_API, params));
//		HttpPost post = HttpClientUtil.getPost(ApiUri.WEIXIN_REFRESH_TOKEN);
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("appid", Configure.getAppid()));
//		params.add(new BasicNameValuePair("grant_type", "refresh_token"));
//		params.add(new BasicNameValuePair("refresh_token", refresh_token));
		try {
			return Beans.httpProxy.syncRequest(gHttpGet, SyncJsonRespHandler.<WeiXinAccessTokenResult> build(WeiXinAccessTokenResult.class));
		} catch (IOException e) {
			throw IllegalConstException.errorException(JiLuCode.REFRESH_TOKEN_FAIL);
		}
	}
}
