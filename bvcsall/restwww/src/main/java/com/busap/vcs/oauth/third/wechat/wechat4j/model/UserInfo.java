package com.busap.vcs.oauth.third.wechat.wechat4j.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.data.enums.Sex;
import com.busap.vcs.oauth.third.wechat.wechat4j.http.HttpClient;
import com.busap.vcs.oauth.third.wechat.wechat4j.http.Response;
import com.busap.vcs.oauth.third.wechat.wechat4j.org.json.JSONException;
import com.busap.vcs.oauth.third.wechat.wechat4j.org.json.JSONObject;

/**
 * 
 * @author shouchen.shan@10020.cn
 * 
 */
public class UserInfo {
	Logger logger = Logger.getLogger(UserInfo.class);

	public Ruser getUserInfo(String openId, String token) throws WeChatException {
		PostParameter[] params = { new PostParameter("access_token", token), new PostParameter("openid", openId) };
		Response response = get("https://api.weixin.qq.com/sns/userinfo", params);
		try {
			return userInfo(response.asJSONObject());
		} catch (JSONException e) {
			log(e, e.getMessage());
			return null;
		}
	}
	
	public String getUnionid(String openId, String token) throws WeChatException {
		PostParameter[] params = { new PostParameter("access_token", token), new PostParameter("openid", openId) };
		Response response = get("https://api.weixin.qq.com/sns/userinfo", params);
		try {
			return response.asJSONObject().getString("unionid");
		} catch (JSONException e) {
			log(e, e.getMessage());
			return null;
		}
	}

	public boolean validate(String openId, String token) throws WeChatException {
		PostParameter[] params = { new PostParameter("access_token", token), new PostParameter("openid", openId) };
		Response response = get("https://api.weixin.qq.com/sns/userinfo", params);
		try {
			String errcode = response.asJSONObject().getString("errcode");
			if (StringUtils.isNotBlank(errcode))
				return false;

			String id = response.asJSONObject().getString("openid");
			if (!id.equals(openId))
				return false;
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	Response get(String url, PostParameter[] params) throws WeChatException {
		HttpClient client = new HttpClient();
		return client.get(url, params);
	}

	public Ruser userInfo(JSONObject json) throws JSONException {
		Ruser r = new Ruser();
		r.setPic(json.getString("headimgurl"));
		r.setSignature(json.getString(""));
		r.setAddr(json.getString("province") + " " + json.getString("city"));
		r.setThirdUserame(json.getString("openid"));
		r.setThirdFrom(ThirdUser.wechat.getValue());// 微信
		r.setPassword(json.getString("openid"));// 因数据库中该字段不能为空，暂时使用用户唯一ID作为密码(无意义)
		r.setUsername(ThirdUser.wechat.getValue() + json.getString("unionid"));
//		r.setUsername(ThirdUser.wechat.getValue() + json.getString("openid"));
		r.setName(json.getString("nickname"));
		String sex = json.getString("sex");
		r.setSex(("1".equals(sex)) ? Sex.男.getName() : (("2".equals(sex)) ? Sex.女.getName() : Sex.不男不女.getName()));
		
		r.setWechatUnionid(json.getString("unionid"));
		return r;

	}

	public void log(Throwable t, Object message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message, t);
		}
	}

	public void log(Object message) {
		if (logger.isDebugEnabled()) {
			logger.debug(message);
		}
	}
}
