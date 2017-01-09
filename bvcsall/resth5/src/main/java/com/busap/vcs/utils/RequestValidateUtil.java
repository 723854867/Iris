package com.busap.vcs.utils;

import java.net.URLDecoder;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.service.JedisService;

@Service("requestValidateUtil")
public class RequestValidateUtil {

	@Autowired
	JedisService jedisService;

	// 刷新网页请求令牌
	public void referReqKey(HttpServletResponse response) {
		// 生成唯一reqKey放入cookie
		String reqKey = UUID.randomUUID().toString();
		setCookie(response, "r_k", reqKey, 10 * 60);
		
		// 将此reqKey放入缓存
		Long score = System.currentTimeMillis(); // 当前时间毫秒
		Long minScore = score - 1000 * 60 * 10; // 当前时间前10分钟
		
		// 保存当前的reqKey，并删除10分钟前的reqKey
		jedisService.setValueToSortedSetAndDel("REQ_KEY_SET",score, reqKey, minScore);
		jedisService.expire("REQ_KEY_SET", 60*10);
	}

	// 验证网页请求令牌
	public boolean validateReqKey(HttpServletRequest request,HttpServletResponse response) {
		//获得请求中的reqKey
		String reqKey = getCookieByName(request, "r_k");
		String referer = request.getHeader("referer");
		
		if (StringUtils.isNotBlank(referer) && referer.contains("resth5") && StringUtils.isNotBlank(reqKey) && jedisService.isSortedSetMemberInShard("REQ_KEY_SET", reqKey) != null) { //如果缓存中存在当前的reqKey，为合法请求，验证通过并刷新reqKey
			jedisService.deleteSortedSetItemFromShard("REQ_KEY_SET", reqKey);
			String newReqKey = UUID.randomUUID().toString();
			setCookie(response, "r_k", newReqKey, 10 * 60);
			jedisService.setValueToSortedSetInShard("REQ_KEY_SET",System.currentTimeMillis(), newReqKey);
			jedisService.expire("REQ_KEY_SET", 60*10);
			return true;
		} else { //如果缓存中不存在当前的reqKey，为非法请求
			return false;
		}
	}

	private String getCookieByName(HttpServletRequest request,String name) {
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equals(name)) {
				return URLDecoder.decode(c.getValue());
			}
		}
		return null;
	}

	private void setCookie(HttpServletResponse response, String name,String value, int expire) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(expire);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
