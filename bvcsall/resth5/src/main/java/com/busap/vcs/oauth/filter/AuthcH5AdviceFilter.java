package com.busap.vcs.oauth.filter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.busap.vcs.service.OAuthService;
import com.busap.vcs.utils.RequestValidateUtil;

/**
 * 权限验证/过滤
 * 
 * @author GhostEX
 * 
 */
public class AuthcH5AdviceFilter extends AuthorizationFilter {

	@Autowired
	private OAuthService oAuthService;
	
	@Resource(name = "requestValidateUtil")
	private RequestValidateUtil requestValidateUtil;
	
	private Logger logger = LoggerFactory.getLogger(AuthcH5AdviceFilter.class);

	@Override
	protected boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue)
			throws Exception {
		
		return true;
		
//		HttpServletRequest hsr = (HttpServletRequest) request;
//		String accessToken=null;
//		String uid=null;
//		// 构建OAuth资源请求
//		OAuthAccessResourceRequest oauthRequest = null;
//		
//		accessToken=hsr.getHeader(OAuth.OAUTH_BEARER_TOKEN);
//		
//		if(StringUtils.isBlank(accessToken)){
//			accessToken= hsr.getParameter(OAuth.OAUTH_BEARER_TOKEN);
//		}
//		
//		uid = hsr.getHeader("uid");
//		
//		if(StringUtils.isBlank(uid)){
//			uid= hsr.getParameter("uid");
//		}
//		
//		if(StringUtils.isBlank(accessToken)){
//			logger.error("h5请求，丢失令牌(access_token)");
//			SecurityUtils.getSubject().logout();
//			return false;
//		}
//		
//		//检测Access Token是否合法
//		if (!oAuthService.checkAccessToken(accessToken)) {
//			logger.error("h5请求，无权限访问资源!");
//			SecurityUtils.getSubject().logout();
//			return false;
//		}
//		//检测accessTokne是否过期
//		if(!oAuthService.isExpireAccessToken(accessToken)){
//			logger.error("h5请求，令牌(access_token)过期!");
//			SecurityUtils.getSubject().logout();
//			return false;
//		}
//		
//		//检测accessToken是否与uid匹配
//		if (!oAuthService.checkAccessTokenAndUid(accessToken,uid)){
//			logger.error("h5请求，令牌与用户id不匹配!");
//			SecurityUtils.getSubject().logout();
//			return false;
//		}
//		return true;
	}

}
