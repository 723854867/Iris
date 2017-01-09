package com.busap.vcs.oauth.filter;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.busap.vcs.service.OAuthService;

/**
 * 权限验证/过滤
 * 
 * @author GhostEX
 * 
 */
public class AuthcAdviceFilter extends AdviceFilter {

	@Autowired
	private OAuthService oAuthService;

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		HttpServletRequest hsr = (HttpServletRequest) request;
		String accessToken=null;
		String uid=null;
		// 构建OAuth资源请求
		OAuthAccessResourceRequest oauthRequest = null;
		
		accessToken=hsr.getHeader(OAuth.OAUTH_BEARER_TOKEN);
		
		if(StringUtils.isBlank(accessToken)){
			accessToken= hsr.getParameter(OAuth.OAUTH_BEARER_TOKEN);
		}
		
		uid = hsr.getHeader("uid");
		
		if(StringUtils.isBlank(uid)){
			uid= hsr.getParameter("uid");
		}

		System.out.println("token:"+accessToken+"  uid:"+uid);
		
		if(StringUtils.isBlank(accessToken)){
			writeMsg(response, "丢失令牌,请重新登陆",HttpServletResponse.SC_BAD_REQUEST+"_5");
			return false;
		}

		//检测Access Token是否合法
		if (!oAuthService.checkAccessToken(accessToken)) {
			writeMsg(response, "无权限访问资源,请重新登陆",HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		//检测accessTokne是否过期
		if(!oAuthService.isExpireAccessToken(accessToken)){
			writeMsg(response, "令牌过期,请重新登陆",HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			return false;
		}

		//检测accessToken是否与uid匹配
		if (!oAuthService.checkAccessTokenAndUid(accessToken,uid)){
			writeMsg(response, "令牌不匹配,请重新登陆",HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
		
		return super.preHandle(request, response);
	}

	private void writeMsg(ServletResponse response, String msgStr, Object code) {
		OutputStream out = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			out = response.getOutputStream();
			Map<String, Object> msg = new HashMap<String, Object>();
			msg.put("code", code);
			msg.put("message", msgStr);
			IOUtils.write(JSONUtils.buildJSON(msg).getBytes("UTF-8"), out);
			out.flush();
		} catch (Exception e) {

		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}
