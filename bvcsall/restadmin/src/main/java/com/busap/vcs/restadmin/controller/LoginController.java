package com.busap.vcs.restadmin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.entity.UserRole;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.UserRoleService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.busap.vcs.service.security.CaptchaAuthenticationToken;
import com.busap.vcs.service.security.IncorrectCaptchaException;
import com.busap.vcs.service.security.Principal;
import com.busap.vcs.service.security.SecurityUtils;
import com.busap.vcs.webcomn.U;
import com.fasterxml.jackson.core.JsonProcessingException;

@Controller
@RequestMapping("login")
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Resource
	private JedisService jedisService;

	@Resource
	private UserRoleService userRoleService;

	@RequestMapping("loginAjax")
	public @ResponseBody Map<String,String> login(String username,String password) throws JsonProcessingException{
		logger.info("username:{},password:{}",username,password);
		Map<String,String> map=new HashMap<String,String>();
		Subject sub=null;
		try {
			//*************************获取系统生成的Captcha*****************************//
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	        HttpSession session = attr.getRequest().getSession(false);
//	        String rcaptcha = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
	        //*************************获取系统生成的Captcha*****************************//
			CaptchaAuthenticationToken token=new CaptchaAuthenticationToken(username, password, null, null, false, null);
//			UsernamePasswordToken token =new UsernamePasswordToken(username,password,false,null);
			sub=SecurityUtils.getSubject();
			sub.login(token);
		} catch (UnknownAccountException e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "没有此账户");
		} catch (LockedAccountException e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "账户被锁定");
		} catch (IncorrectCaptchaException e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "验证码错误");
		} catch (DisabledAccountException e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "此账户不可用");
		} catch (IncorrectCredentialsException e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "账户和密码不匹配");
		} catch (Exception e) {
			logger.error("login error!!!!!!!!!!!!!!!!!!!",e);
			map.put("message", "系统异常");
		} finally{
			if(map.containsKey("message")){
				map.put("result", "error");
				return map;
			}
		}
		map.put("result", "success");
		Principal p=(Principal)sub.getPrincipal();
		U.setAttribute(com.busap.vcs.webcomn.Constants.SESSION_UID, p.getId());
        U.setAttribute(com.busap.vcs.webcomn.Constants.SESSION_UNAME, p.getUsername());
		UserRole ur = new UserRole();
		ur.setUser(p.getId());
		String checkGroupId = jedisService.get(BicycleConstants.CHECK_GROUP_ID);
		if(StringUtils.isBlank(checkGroupId)){
			jedisService.set(BicycleConstants.CHECK_GROUP_ID,"5");
			checkGroupId = jedisService.get(BicycleConstants.CHECK_GROUP_ID);
		}
		ur.setRoles(Long.valueOf(checkGroupId));
		List<UserRole> userRoleList = userRoleService.queryUserRoles(ur);
		if(!userRoleList.isEmpty()) {
			jedisService.setValueToSortedSetInShard(BicycleConstants.CHECK_GROUP, System.currentTimeMillis(), p.getUsername());
		}
		return map;
	}
	
	@RequestMapping("logout")
	public String logout(){
		UserRole ur = new UserRole();
		ur.setUser(U.getUid());
		String checkGroupId = jedisService.get(BicycleConstants.CHECK_GROUP_ID);
		if(StringUtils.isBlank(checkGroupId)){
			jedisService.set(BicycleConstants.CHECK_GROUP_ID,"5");
			checkGroupId = jedisService.get(BicycleConstants.CHECK_GROUP_ID);
		}
		ur.setRoles(Long.valueOf(checkGroupId));
		List<UserRole> userRoleList = userRoleService.queryUserRoles(ur);
		if(!userRoleList.isEmpty()) {
			jedisService.deleteSortedSetItemFromShard(BicycleConstants.CHECK_GROUP, U.getUname());
		}
		SecurityUtils.getSubject().logout();
		return "redirect:login.jsp";
	}
}
