package com.busap.vcs.oauth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.busap.vcs.service.OAuthService;

/**
 * token验证
 * 
 * @author shanshouchen
 * 
 */
public class ShiroInterceptor implements HandlerInterceptor {

	@Autowired
	private OAuthService oAuthService;

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// do nothing
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// do nothing
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {

		// check accessToken

		// 构建OAuth资源请求
		OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(
				arg0, ParameterStyle.QUERY);
		// 获取Access Token
		String accessToken = oauthRequest.getAccessToken();

		// 验证Access Token
		if (!oAuthService.checkAccessToken(accessToken)) {
			// 如果不存在/过期了，返回未验证错误，需重新验证
			return false;
		}
		return true;
	}

}
