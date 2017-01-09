package com.busap.vcs.oauth.filter;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.exceptions.JedisDataException;

import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.service.JedisService;

/**
 * 登录过滤器
 * 
 * @author shouchen.shan@10020.cn
 * 
 */
public class LogoutFilter extends AdviceFilter {

	@Autowired
	private JedisService jedisService;

	private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest hsr = (HttpServletRequest) request;
		String accessToken = null;
		String dataFrom = null;
		/* 优先从Header中取数据 */
		accessToken = hsr.getHeader(OAuth.OAUTH_BEARER_TOKEN);
		dataFrom = hsr.getHeader("data_from");

		if (StringUtils.isBlank(accessToken)) {
			accessToken = hsr.getParameter(OAuth.OAUTH_BEARER_TOKEN);
		}
		if (StringUtils.isBlank(dataFrom)) {
			dataFrom = hsr.getParameter("data_from");
		}

		Map<String, String> tokenInfo = jedisService.getMapByKey(accessToken);

		if (tokenInfo == null) {
			createResponse(response, "令牌丢失/非法", HttpServletResponse.SC_BAD_REQUEST + "_5");
			if (log.isInfoEnabled())
				log.info("非法令牌:[" + accessToken + "]");
			return false;
		}
		String username = tokenInfo.get("username");
		try {
			clean(generatName(username + "_token"), accessToken);// 清除令牌(token)
		} catch (JedisDataException e) {
			if (log.isErrorEnabled()) {
				log.error("帐号[" + username + "]登出失败", e.getMessage());
			}
		}

		// 本地账户(需要删除code信息)
		if (!ThirdUser.qq.getValue().equals(dataFrom) && !ThirdUser.sina.getValue().equals(dataFrom) && !ThirdUser.wechat.getValue().equals(dataFrom)) {
			String code = jedisService.get(generatName(username + "_code"));

			try {
				clean(generatName(username + "_code"), code);// 清楚授权码(code)
			} catch (JedisDataException e) {
				if (log.isErrorEnabled()) {
					log.error("帐号[" + username + "]登出失败", e.getMessage());
				}
			}
		}
		// 响应客户端
		createResponse(response, "成功", HttpServletResponse.SC_OK);

		if (log.isInfoEnabled())
			log.info("帐号：[" + username + "]成功退出");
		return false;
	}

	/* 清除登录信息，包括授权码(authCode)和令牌(access_token) */
	protected void clean(String key, String access_or_code) {
		if (StringUtils.isNotBlank(key))
			jedisService.delete(key);
		if (StringUtils.isNotBlank(access_or_code))
			jedisService.delete(access_or_code);

	}

	private void createResponse(ServletResponse response, String msgStr, Object code) {
		writeMsg(response, msgStr, code);
	}

	private String generatName(String key) {
		return "oltu_" + key;
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
