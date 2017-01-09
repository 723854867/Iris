package com.busap.vcs.oauth.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.ClientService;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.OAuthService;

/**
 * 授权
 * 
 * @author shanshouchen
 * 
 */
@RestController
public class AuthorizeController {
	private Logger logger = LoggerFactory.getLogger(AuthorizeController.class);
	@Autowired
	private OAuthService oAuthService;
	@Autowired
	private ClientService clientService;
	@Autowired
    JedisService jedisService;

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/authorize", method = RequestMethod.POST)
	public HttpEntity authorize(HttpServletRequest request) throws OAuthSystemException {
		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

			// 检查传入的客户端id是否正确
			if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
				if (logger.isInfoEnabled())
					logger.info("无效的客户端:" + oauthRequest.getClientId());
				Map<String, Object> msg = generateMsg("无效的客户端", HttpServletResponse.SC_BAD_REQUEST + "_1");
				return new ResponseEntity(msg, HttpStatus.OK);
			}

			Subject subject = SecurityUtils.getSubject();
			if (!validation(request)) {
				if (logger.isInfoEnabled())
					logger.info("无效登陆名:" + request.getParameter("username"));
				Map<String, Object> msg = generateMsg("无效的登录名", HttpServletResponse.SC_BAD_REQUEST + "_3");
				return new ResponseEntity(msg, HttpStatus.OK);
			}

			Map<String, Object> result = new HashMap<String, Object>();
			if (!login(subject, request, result)) {
				return new ResponseEntity(result, HttpStatus.OK);
			}

			String username = (String) subject.getPrincipal();
			// 生成授权码
			String authorizationCode = null;
			// responseType目前仅支持CODE，另外还有TOKEN
			String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				// 存储授权码
				oAuthService.addAuthCode(authorizationCode, username);
			}

			Map<String, Object> msg = generateMsg(null, HttpServletResponse.SC_OK);

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("authorization_code", authorizationCode);
			msg.put("result", data);
			return new ResponseEntity(msg, HttpStatus.OK);
		} catch (OAuthProblemException e) {
			logger.error(e.getMessage(), e);
			Map<String, Object> msg = generateMsg("内部错误", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return new ResponseEntity(msg, HttpStatus.OK);
		}
	}

	/* 验证用户名密码的合法性 */
	private boolean validation(HttpServletRequest request) {
		if ("get".equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String username = request.getParameter("username");
		String areaCode = request.getParameter("areaCode");

		if (org.apache.commons.lang3.StringUtils.isBlank(username)) {
			return false;
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode)) { //国际短信
			Pattern pattern = Pattern.compile("[0-9]*");
		    return pattern.matcher(username).matches();   
		} else {
			Pattern pattern = Pattern.compile("^1[0-9]{10}$");
			Matcher matcher = pattern.matcher(username);
			if (matcher.matches()) {
				return true;
			}
		}
		return false;
	}

	// 用户登陆验证 1:用户未注册;2:用户名密码错误
	private boolean login(Subject subject, HttpServletRequest request, Map<String, Object> result) {
		if ("get".equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String areaCode = request.getParameter("areaCode");
		
		String pwdWrongTimes = jedisService.get(BicycleConstants.PWD_WRONG_TIMES_+username);
		if (pwdWrongTimes != null && Integer.parseInt(pwdWrongTimes) >=5 ){
			result.putAll(generateMsg("密码错误超过5次，请1小时后重试", HttpServletResponse.SC_BAD_REQUEST + "_4"));
			return false;
		}

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			if (logger.isInfoEnabled())
				logger.info("登陆名或者密码错误");
			result.putAll(generateMsg("登录名或密码错误", HttpServletResponse.SC_BAD_REQUEST + "_4"));
			return false;
		}
		
		if (org.apache.commons.lang3.StringUtils.isNotBlank(areaCode) && !"0086".equals(areaCode) && !"86".equals(areaCode) ) {  //国际短信加区号
			username = areaCode + username;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			subject.login(token);
			return true;
		} catch (UnknownAccountException unknow) {
			// 用户未注册
//			logger.error("用户未被注册", unknow);
			logger.warn("用户未被注册");
			result.putAll(generateMsg("用户未注册", HttpServletResponse.SC_BAD_REQUEST + "_6"));
			return false;
		} catch (LockedAccountException disabledAccount) {
			// 锁定的账户
//			logger.error("账户已被锁定", disabledAccount);
			logger.warn("账户已被锁定");
			result.putAll(generateMsg("账户已被锁定", HttpServletResponse.SC_BAD_REQUEST + "_7"));
			return false;
		} catch (DisabledAccountException lockedAccount) {
			// 禁用的账户
//			logger.error("账户已被禁用", lockedAccount);
			logger.warn("账户已被禁用");
			result.putAll(generateMsg("你已被封号，请联系管理员", HttpServletResponse.SC_BAD_REQUEST + "_8"));
			return false;
		} catch (ExpiredCredentialsException expiredCrdentials) {
			// 过期的账户
//			logger.error("账户已过期", expiredCrdentials);
			logger.warn("账户已过期");
			result.putAll(generateMsg("账户已过期", HttpServletResponse.SC_BAD_REQUEST + "_9"));
			return false;
		} catch (IncorrectCredentialsException incorrect) {
			// 用户名密码错误
//			logger.error("用户名或者密码错误", incorrect);
			logger.warn("用户名或者密码错误");
			result.putAll(generateMsg("登录名或密码错误", HttpServletResponse.SC_BAD_REQUEST + "_4"));
			jedisService.incr(BicycleConstants.PWD_WRONG_TIMES_+username);
			jedisService.expire(BicycleConstants.PWD_WRONG_TIMES_+username, 60*60);
			return false;
		} catch (Exception e) {
			logger.error("内部错误", e);
			// 其他错误(内部错误)
			result.putAll(generateMsg("内部错误", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
			return false;
		}
	}

	private Map<String, Object> generateMsg(String msg, Object code) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", code);
		String message = msg != null ? msg : "";
		result.put("message", message);

		return result;
	}
}
