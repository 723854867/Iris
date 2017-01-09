package com.busap.vcs.oauth.third.qq;

import org.apache.commons.lang3.StringUtils;

import com.qq.connect.QQConnect;
import com.qq.connect.QQConnectException;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.PostParameter;

public class MyUserInfo extends QQConnect {

	private static final long serialVersionUID = 335582132021911444L;

	public MyUserInfo(String token, String openID) {
		super(token, openID);
	}

	private MyQQUserInfoBean getUserInfo(String openid, String appId) throws QQConnectException {
		if(StringUtils.isBlank(appId)){
			appId = QQConnectConfig.getValue("app_ID");
		}else{
			appId = "1105498927";
		}
		System.out.println("openid=" + openid + "&appId=" + appId);
		return new MyQQUserInfoBean(this.client.get(
				QQConnectConfig.getValue("getUserInfoURL"),
				new PostParameter[] { new PostParameter("openid", openid), new PostParameter("oauth_consumer_key", appId),
						new PostParameter("access_token", this.client.getToken()), new PostParameter("format", "json") }).asJSONObject());
	}

	public MyQQUserInfoBean getUserInfo(String appId) throws QQConnectException {
		return getUserInfo(this.client.getOpenID(), appId);
	}
	
	public MyQQUserInfoBean getUserInfo() throws QQConnectException {
		return getUserInfo(this.client.getOpenID(), "");
	}

}
