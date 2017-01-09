package com.busap.vcs.oauth.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;
import com.busap.vcs.service.RuserService;

/**
 * 令牌
 * 
 */
@RestController
public class AccessTokenController {
	private Logger logger = LoggerFactory.getLogger(AccessTokenController.class);
	
	@Autowired
	private OAuthService oAuthService;

	@Autowired
	private RuserService ruserService;

	@Resource(name = "jedisService")
	private JedisService jedisService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/accessToken", method = RequestMethod.POST)
	public HttpEntity token(HttpServletRequest request) throws OAuthSystemException {
		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

			// 检查提交的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				if (logger.isInfoEnabled())
					logger.info("无效的客户端:" + oauthRequest.getClientId());
				Map<String, Object> msg = generateMsg("无效的客户端",HttpServletResponse.SC_BAD_REQUEST + "_1");
				return new ResponseEntity(msg, HttpStatus.OK);
			}
			
			// 检查客户端安全KEY是否正确
			if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {

				Map<String, Object> msg = generateMsg("无效的客户端",HttpServletResponse.SC_BAD_REQUEST + "_1");
				return new ResponseEntity(msg, HttpStatus.OK);
			}
			
			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);

			// 检查验证类型(访问许可的类型)，总共有三种验证类型AUTHORIZATION_CODE/PASSWORD/REFRESH_TOKEN,目前只提供对AUTHORIZATION_CODE类型的支持，
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!oAuthService.checkAuthCode(authCode)) {
					if (logger.isInfoEnabled())
						logger.info("无效的授权码:" + authCode);
					Map<String, Object> msg = generateMsg("无效的授权码",HttpServletResponse.SC_BAD_REQUEST + "_2");
					return new ResponseEntity(msg, HttpStatus.OK);
				}
			}
			
			Map<String, String> authInfo = oAuthService.getAuthInfoByAuthCode(authCode);
			if (authInfo == null || authInfo.isEmpty()) {
				if (logger.isInfoEnabled())
					logger.info("无效的授权码:" + authCode);
				Map<String, Object> msg = generateMsg("无效的授权码",HttpServletResponse.SC_BAD_REQUEST + "_2");
				return new ResponseEntity(msg, HttpStatus.OK);
			}
			
			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());

			final String accessToken = oauthIssuerImpl.accessToken();
			
			// 存储Token
			oAuthService.addAccessToken(accessToken,oAuthService.getUsernameByAuthCode(authCode));

			Map<String, Object> msg = generateMsg(null,HttpServletResponse.SC_OK);
			
			// 用户信息放到redis
			putUserToRedis(request,oAuthService.getUsernameByAuthCode(authCode));
			
			// 更新用户登陆时间
			ruserService.updateLoginDate(oAuthService.getUsernameByAuthCode(authCode));

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("access_token", accessToken);
			data.put(OAuth.OAUTH_EXPIRES_IN, oAuthService.getExpireIn());
			data.put(OAuth.OAUTH_BEARER_TOKEN, accessToken);
			data.put("isUserInfoCompletion", "Y");
			msg.put("result", data);
			return new ResponseEntity(msg, HttpStatus.OK);

		} catch (OAuthProblemException e) {
			if (logger.isErrorEnabled())
				logger.error(e.getMessage(), e);
			// 构建错误响应
			Map<String, Object> msg = generateMsg("内部错误",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ResponseEntity(msg, HttpStatus.OK);
		}
	}

	private Map<String, Object> generateMsg(String msg, Object code) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		String message = msg != null ? msg : "";
		result.put("message", message);

		return result;
	}

	// 将用户信息放入redis
	private void putUserToRedis(HttpServletRequest request, String username) {
		logger.info("put user to redis,username is {}", username);
		Ruser user = ruserService.findByUsername(username);
		if (user != null) {
			String key = BicycleConstants.USER_INFO + user.getId();
			jedisService.saveAsMap(key, user);
			
			// 将用户手机平台信息放入redis
			String platform = request.getParameter("osVersion");
			// 将设备唯一id放到redis
			String deviceUuid = request.getParameter("deviceUuid");
			if (platform != null) {
				logger.info("put user to redis,platform is {}", platform);
				if (platform.contains("android"))
					jedisService.setValueToMap(key, "platform", "android");
				else if (platform.contains("ios"))
					jedisService.setValueToMap(key, "platform", "ios");
			}
			if (deviceUuid != null) {
				logger.info("put user to redis,deviceUuid is {}", deviceUuid);
				jedisService.setValueToMap(key, "deviceUuid", deviceUuid);
			} else {
				logger.info("put user to redis,deviceUuid is {}", "");
				jedisService.setValueToMap(key, "deviceUuid", "");
			}
		}
	}
}
