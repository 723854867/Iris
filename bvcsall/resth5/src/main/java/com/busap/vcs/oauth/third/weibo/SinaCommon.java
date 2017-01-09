package com.busap.vcs.oauth.third.weibo;

import javax.annotation.Resource;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.oauth.third.ThirdPartCommon;
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
	public String getOldToken(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
