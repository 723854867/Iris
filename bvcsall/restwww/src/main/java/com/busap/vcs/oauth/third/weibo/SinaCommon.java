package com.busap.vcs.oauth.third.weibo;

import java.security.InvalidParameterException;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.data.enums.Sex;
import com.busap.vcs.oauth.third.ThirdPartCommon;
import com.busap.vcs.oauth.third.weibo.weibo4j.Account;
import com.busap.vcs.oauth.third.weibo.weibo4j.Users;
import com.busap.vcs.oauth.third.weibo.weibo4j.model.User;
import com.busap.vcs.oauth.third.weibo.weibo4j.model.WeiboException;
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONException;
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONObject;
import com.busap.vcs.oauth.web.exception.ThirdPartException;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;

/**
 * 新浪微博登陆验证
 * 
 * @author GhostEX
 * 
 */
@Service("thirdPartCommon_sina")
public class SinaCommon implements ThirdPartCommon {

	@Resource(name = "oAuthService")
	private OAuthService oAuthService;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Override
	public boolean validate(String access_token, String uid)
			throws InvalidParameterException, ThirdPartException, JSONException {
		if (StringUtils.isBlank(access_token) || StringUtils.isBlank(uid))
			throw new InvalidParameterException("令牌或者用户Id无效");
		Account am = new Account(access_token);
		try {
			JSONObject uIdInfo = am.getUid();
			String tmp_uid = uIdInfo.getString("uid");
			if (uid.equals(tmp_uid)) {
				return true;
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			throw new ThirdPartException(e);
		} catch (JSONException e) {
			throw e;
		}

		return false;
	}

	@Override
	public Ruser getInfo(String access_token, String uid) {
		Users um = new Users(access_token);
		try {
			User user = um.showUserById(uid);
			return createRuser(user, access_token);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Ruser createRuser(User user, String access_token) {
		Ruser r = new Ruser();
		r.setPic(user.getProfileImageUrl());
		r.setSignature(user.getDescription());
		r.setAddr(user.getLocation());
		r.setThirdUserame(user.getId());
		r.setThirdFrom(ThirdUser.sina.getValue());// 新浪注册
		r.setPassword(access_token);// 因数据库中该字段不能为空，暂时使用令牌作为密码(无意义)
		r.setUsername(ThirdUser.sina.getValue() + user.getId());
		r.setName(user.getName());
		r.setSex(("n".equals(user.getGender())) ? Sex.不男不女.getName() : (("m"
				.equals(user.getGender())) ? Sex.男.getName() : Sex.女.getName()));
		return r;
	}

	@Override
	public String createAndStoreAccessToken(String uid, String expire)
			throws OAuthSystemException {
		// 生成Access Token
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		final String accessToken = oauthIssuerImpl.accessToken();
		// 存储Token
		oAuthService.addAccessToken(accessToken, ThirdUser.sina.getValue()
				+ uid, expire);
		return accessToken;
	}

	@Override
	public boolean isExist(String uid) {
		boolean isExist = ruserService.countByThirdName(uid,
				ThirdUser.sina.getValue());
		return isExist;
	}

	@Override
	public String thirdPartName() {
		return ThirdUser.sina.getValue();
	}

	@Override
	public String getUnionid(String access_token, String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
