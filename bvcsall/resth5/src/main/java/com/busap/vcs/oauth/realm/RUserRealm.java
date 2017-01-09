package com.busap.vcs.oauth.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.service.RuserService;

public class RUserRealm extends AuthorizingRealm {

	@Autowired
	private RuserService ruserService;
	

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		//String username = (String) principals.getPrimaryPrincipal();

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 暂时不加权限
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

        String username = (String)token.getPrincipal();

        Ruser user = ruserService.findByUsername(username);

        if(user == null) {
        	user = ruserService.findByBandPhone(username);//绑定手机号的,用绑定的手机号登陆
        	if (user == null) {
        		throw new UnknownAccountException();//没找到帐号
        	}
        }
//        //自动生成的帐号不允许直接登录
//        if(ThirdUser.sina.getValue().equals(user.getThirdFrom()) || ThirdUser.qq.getValue().equals(user.getThirdFrom())|| ThirdUser.wechat.getValue().equals(user.getThirdFrom())){
//        	throw new UnknownAccountException();
//        }
        //封号
        if(user.getStat()==2){
        	throw new DisabledAccountException();
        }
        /*
         * 常见的帐号异常
         * DisabledAccountException	| 禁用的账户
         * LockedAccountException | 锁定的账户
         * UnknownAccountException | 错误的账户
         * ExpiredCredentialsException | 过期的账户
         * 
         */

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
       /*
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
        */
        return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), getName());
        
	}
	
	 @Override
	    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
	        super.clearCachedAuthorizationInfo(principals);
	    }

	    @Override
	    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
	        super.clearCachedAuthenticationInfo(principals);
	    }

	    @Override
	    public void clearCache(PrincipalCollection principals) {
	        super.clearCache(principals);
	    }

	    public void clearAllCachedAuthorizationInfo() {
	        getAuthorizationCache().clear();
	    }

	    public void clearAllCachedAuthenticationInfo() {
	        getAuthenticationCache().clear();
	    }

	    public void clearAllCache() {
	        clearAllCachedAuthenticationInfo();
	        clearAllCachedAuthorizationInfo();
	    }

}
