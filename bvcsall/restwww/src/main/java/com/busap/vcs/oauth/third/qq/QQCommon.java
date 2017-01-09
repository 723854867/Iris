package com.busap.vcs.oauth.third.qq;

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
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONException;
import com.busap.vcs.oauth.web.exception.ThirdPartException;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;

@Service("thirdPartCommon_qq")
public class QQCommon implements ThirdPartCommon {

	@Resource(name = "oAuthService")
	private OAuthService oAuthService;

	@Resource(name = "ruserService")
	private RuserService ruserService;

	@Override
	public boolean validate(String access_token, String uid)
			throws InvalidParameterException, ThirdPartException, JSONException {
		if (StringUtils.isBlank(access_token) || StringUtils.isBlank(uid))
			throw new InvalidParameterException("令牌或开放ID不能为空");
		OpenID openIdCommon = new OpenID(access_token);
		String openId = null;
		try {
			openId = openIdCommon.getUserOpenID();
		} catch (QQConnectException e) {
			e.printStackTrace();
			throw new ThirdPartException(e);
		}
		if (uid.equals(openId)) {
			return true;
		}
		return false;
	}

	@Override
	public Ruser getInfo(String access_token, String uid) {
		MyUserInfo userInfo=new MyUserInfo(access_token, uid);
//		UserInfo userInfo = new UserInfo(access_token, uid);
		try {
//			UserInfoBean userInfoBean = userInfo.getUserInfo();
			MyQQUserInfoBean userInfoBean = userInfo.getUserInfo();
			
			return createRuser(userInfoBean, access_token, uid);
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Ruser getInfo(String access_token, String uid, String appId) {
		MyUserInfo userInfo=new MyUserInfo(access_token, uid);
//		UserInfo userInfo = new UserInfo(access_token, uid);
		try {
//			UserInfoBean userInfoBean = userInfo.getUserInfo();
			MyQQUserInfoBean userInfoBean = userInfo.getUserInfo(appId);
			
			return createRuser(userInfoBean, access_token, uid);
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Ruser createRuser(MyQQUserInfoBean user, String access_token, String uid) {
		Ruser r = new Ruser();

//		r.setPic(user.getAvatar().getAvatarURL100());
		r.setPic(user.getQq_pic());
		r.setSignature("");
		r.setAddr("");
		r.setThirdUserame(uid);
		r.setThirdFrom(ThirdUser.qq.getValue());// QQ注册
		r.setPassword(access_token);// 因数据库中该字段不能为空，暂时使用令牌作为密码(无意义)
		r.setUsername(ThirdUser.qq.getValue() + uid);
		r.setName(user.getNickname());
		r.setSex(("女".equals(user.getGender())) ? Sex.女.getName() : Sex.男
				.getName());
		return r;
	}

	@Override
	public String createAndStoreAccessToken(String uid, String expire)
			throws OAuthSystemException {
		// 生成Access Token
		OAuthIssuer oauthIssuserImpl = new OAuthIssuerImpl(new MD5Generator());
		final String accessToken = oauthIssuserImpl.accessToken();
		// 存储Token
		oAuthService.addAccessToken(accessToken, ThirdUser.qq.getValue() + uid,
				expire);
		return accessToken;
	}

	@Override
	public boolean isExist(String uid) {
		boolean isExist = ruserService.countByThirdName(uid,
				ThirdUser.qq.getValue());
		return isExist;
	}

	@Override
	public String thirdPartName() {
		return ThirdUser.qq.getValue();
	}

	@Override
	public String getUnionid(String access_token, String uid) {
		// TODO Auto-generated method stub
		return null;
	}

}
