package com.busap.vcs.restadmin.security;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.busap.vcs.restadmin.utils.CommonUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.busap.vcs.data.entity.User;
import com.busap.vcs.service.UserService;
import com.busap.vcs.service.security.CaptchaAuthenticationToken;
import com.busap.vcs.service.security.Principal;
import com.busap.vcs.service.security.SubjectPermissionProvider;
//import com.google.code.kaptcha.Constants;

/**
 * 基于口令和验证码的Realm<br/>
 * 根据username在数据库中取出User信息<br/>
 * 根据User信息取出Role信息<br/>
 * 根据Role信息取出Permission信息<br/>
 */
public class WebCaptchaUserAuthorizingRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory
			.getLogger(WebCaptchaUserAuthorizingRealm.class);
	
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "userService")
    private SubjectPermissionProvider subjectPermissionProvider;


//    private boolean validateCaptcha(String captcha) {
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        if (attr == null) {
//            return false;
//        }
//        HttpSession session = attr.getRequest().getSession(false);
//        if (session == null) {
//            return false;
//        }
//        String rcaptcha = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        if (rcaptcha == null) {
//            return false;
//        }
//        if (StringUtils.equals(rcaptcha, captcha)) {
//            return true;
//        }
//        return false;
//    }

    /**
     * 进行认证，获取认证信息
     *
     * @param token the token
     * @return the authentication info
     */
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) {
        CaptchaAuthenticationToken localAuthenticationToken = (CaptchaAuthenticationToken) token;
        String username = localAuthenticationToken.getUsername();
        String password = new String(localAuthenticationToken.getPassword());
//        String captcha = localAuthenticationToken.getCaptcha();
        //String host = localAuthenticationToken.getHost(); // 2015-10-20 11:38 huoshanwei注释

//        if (!validateCaptcha(captcha)) {
//            throw new IncorrectCaptchaException("captcha");
//        }
        if ((username != null) && (password != null)) {
            User user = userService.findByUsername(username);
            if (user == null) {
                throw new UnknownAccountException();
            }
            if (!user.getIsEnabled().booleanValue()) {
                throw new DisabledAccountException();
            }

            if (user.getIsLocked().booleanValue()) { //锁定账户

                // 对全局配置进行检查, 是否存在全局的账号锁定策略
                int accountLockTime = 30; //锁定30分钟
                Date lockedDateTmp = user.getLockedDate();
                Date lockedDate = DateUtils.addMinutes(lockedDateTmp,
                        accountLockTime);
                if (new Date().after(lockedDate)) {
                    user.setLoginFailureCount(0);
                    user.setIsLocked(false);
                    user.setLockedDate(null);
                    userService.update(user);
                } else {
                    throw new LockedAccountException();
                }
            }

            if (!DigestUtils.md5Hex(password).equals(user.getPassword())) { // 验证密码,5次锁死
                Integer loginFailureCount = user.getLoginFailureCount() + 1;
                if (loginFailureCount >= 5) {
                    user.setIsLocked(true);
                    user.setLockedDate(new Date());
                }
                user.setLoginFailureCount(loginFailureCount);
                userService.update(user);
                throw new IncorrectCredentialsException("password");
            }
            user.setLoginIp(CommonUtils.getRemoteIp());
            user.setLoginDate(new Date());
            user.setLoginFailureCount(0);
            userService.update(user);
            return new SimpleAuthenticationInfo(new Principal(user.getId(),
                    username), password, getName());
        }
        throw new UnknownAccountException();
    }

    /**
     * 进行授权，获取授权信息
     *
     * @param principals the principals
     * @return the authorization info
     */
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
    	logger.info("==========||||||||||========invoke doGetAuthorizationInfo==============||||||||||==============");
        Principal principal = (Principal) principals.fromRealm(getName())
                .iterator().next();
        if (principal != null) {
            List<String> authorities = subjectPermissionProvider.findPermissionsId(principal
                    .getId());
            if (authorities != null) {
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                simpleAuthorizationInfo.addStringPermissions(authorities);
                return simpleAuthorizationInfo;
            }
        }
        return null;
    }
}
