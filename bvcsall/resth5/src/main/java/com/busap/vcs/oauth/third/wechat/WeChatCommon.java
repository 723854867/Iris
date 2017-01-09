package com.busap.vcs.oauth.third.wechat;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.oauth.third.ThirdPartCommon;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;

/**
 * 微信登录验证
 * 
 * @author shouchen.shan@10020.cn
 * 
 */
@Service("thirdPartCommon_wechat")
public class WeChatCommon implements ThirdPartCommon {
	Logger logger = Logger.getLogger(WeChatCommon.class);

	@Resource(name = "oAuthService")
	private OAuthService oAuthService;

	@Resource(name = "ruserService")
	private RuserService ruserService;
	
	@Autowired
    JedisService jedisService;

	@Override
	public String createAndStoreAccessToken(String uid, String expire) throws OAuthSystemException {
		// 生成Access Token
		OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
		final String accessToken = oauthIssuerImpl.accessToken();
		// 存储Token
		oAuthService.addAccessToken(accessToken, ThirdUser.wechat.getValue() + uid, expire);
		return accessToken;
	}

	@Override
	public boolean isExist(String uid) {
		boolean isExist = ruserService.countByThirdName(uid, ThirdUser.wechat.getValue());
		return isExist;
	}

	@Override
	public String thirdPartName() {
		return ThirdUser.wechat.getValue();
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

	@Override
	public String getOldToken(String key) {
		String token = jedisService.get("oltu_" + key + "_token");
		return token;
	}

}
