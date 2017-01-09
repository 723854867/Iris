package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.ClientService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;

@Service("oAuthService")
public class OAuthServiceImpl implements OAuthService {

	private long expireIn = 3600 * 24 * 365L;

	public void setExpireIn(long expireIn) {
		this.expireIn = expireIn;
	}

	@Autowired
	private JedisService jedisService;
	
	@Resource(name="ruserService")
	private RuserService ruserService;

	@Autowired
	private ClientService clientService;

	public void addAuthCode(String authCode, String username) {
		/* 清理授权码 */
		clean(generatName(username + "_code"), authCode);
		Map<String, String> authCodeInfo = new HashMap<String, String>();
		authCodeInfo.put("username", username);
		authCodeInfo.put("create_time", System.currentTimeMillis() + "");
		jedisService.setValueToMap(authCode, authCodeInfo);
	}

	public void addAccessToken(String accessToken, String username) {
		addAccessToken(accessToken, username, null);
	}

	public Map<String, String> getAuthInfoByAuthCode(String authCode) {
		return jedisService.getMapByKey(authCode);
	}

	public Map<String, String> getAccessTokenInfoByAccessToken(String accessToken) {
		return jedisService.getMapByKey(accessToken);
	}

	public String getUsernameByAuthCode(String authCode) {
		Map<String, String> authInfo = getAuthInfoByAuthCode(authCode);
		if (authInfo != null) {
			return authInfo.get("username");
		}
		return null;
	}

	public String getUsernameByAccessToken(String accessToken) {
		Map<String, String> accessTokenInfo = getAccessTokenInfoByAccessToken(accessToken);
		if (accessTokenInfo != null) {
			return accessTokenInfo.get("username");
		}
		return null;
	}

	public boolean checkAuthCode(String authCode) {
		return !getAuthInfoByAuthCode(authCode).isEmpty();
	}

	public boolean checkAccessToken(String accessToken) {
		return !getAccessTokenInfoByAccessToken(accessToken).isEmpty();
	}

	public boolean checkClientId(String clientId) {
		return clientService.findByClientId(clientId) != null;
	}

	public boolean checkClientSecret(String clientSecret) {
		return clientService.findByClientSecret(clientSecret) != null;
	}

	public long getExpireIn() {
		// return 3600 * 24 * 365L;
		return expireIn;
	}

	public boolean isExpireAccessToken(String accessToken) {
		long accessTokenTime = getAccessTokenCreateTime(accessToken);
		long currentTime = System.currentTimeMillis();
		// 超时(从reids获得设置的超时时间，如果redis中没有设置超时时间，按照默认超时时间处理)
		long tmp_expirein = getAccessTokenExpire(accessToken);
		if (-1 == tmp_expirein)
			tmp_expirein = getExpireIn();
		if ((currentTime - accessTokenTime) / 1000 >= tmp_expirein) {
			return false;
		}
		return true;
	}

	private long getAccessTokenCreateTime(String accessToken) {
		Map<String, String> accessTokenInfo = getAccessTokenInfoByAccessToken(accessToken);
		if ((accessTokenInfo != null) && (accessTokenInfo.get("create_time") != null)) {
			return Long.valueOf(accessTokenInfo.get("create_time"));
		}
		return 0;
	}

	private long getAccessTokenExpire(String accessToken) {
		Map<String, String> accessTokenInfo = getAccessTokenInfoByAccessToken(accessToken);
		if (accessTokenInfo != null) {
			String expire = accessTokenInfo.get("expire");
			if (StringUtils.isNotBlank(expire) && NumberUtils.isDigits(expire)) {
				return Long.valueOf(accessTokenInfo.get("expire"));
			}
			return -1;
		}
		return 0;
	}

	private String generatName(String key) {
		return "oltu_" + key;
	}

	public String getAccessTokenInfoByUsername(String username) {
		return jedisService.get("oltu_" + username + "_token");
	}

	/* 重写该用户在redis的信息，包括授权码(authCode)和令牌(access_token) */
	private void clean(String key, String code) {
		String access_or_auth_code = jedisService.get(key);
		if (StringUtils.isNotBlank(access_or_auth_code)) {
			jedisService.delete(access_or_auth_code);
		}
		jedisService.set(key, code);
	}

	@Override
	public void addAccessToken(String accessToken, String username, String expire) {
		/* 清理 */
		clean(generatName(username + "_token"), accessToken);

		Ruser ruser = ruserService.findByUsername(username);
		
		Map<String, String> tokenInfo = new HashMap<String, String>();
		tokenInfo.put("username", username);
		tokenInfo.put("id", ruser == null?"":String.valueOf(ruser.getId())); //将用户id放入redis
		tokenInfo.put("create_time", System.currentTimeMillis() + "");
		if (expire != null)
			tokenInfo.put("expire", expire);
		jedisService.setValueToMap(accessToken, tokenInfo);

	}

	@Override
	public boolean checkAccessTokenAndUid(String accessToken, String uid) {
		if(uid == null || "".equals(uid)){
			return true;
		} else {
			String id = jedisService.getMapByKey(accessToken).get("id");
			if (id != null && id.equals(uid)){
				return true;
			}
		}
		return false;
	}
}