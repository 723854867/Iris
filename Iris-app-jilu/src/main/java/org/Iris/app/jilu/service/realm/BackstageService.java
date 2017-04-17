package org.Iris.app.jilu.service.realm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.storage.domain.BgUser;
import org.Iris.app.jilu.storage.redis.BgkeyGenerator;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.util.common.IrisSecurity;
import org.Iris.util.lang.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class BackstageService implements Beans{

	/**
	 * 登陆
	 * @param account
	 * @param password
	 * @return
	 */
	public String login(String account, String password,HttpServletRequest request) {
		BgUser user = getBgUser(account);
		if(user == null)
			throw IllegalConstException.errorException(JiLuParams.ACCOUNT);
		if(!user.getPassword().equals(IrisSecurity.toMd5(password)))
			throw IllegalConstException.errorException(JiLuParams.PASSWORD);
		user.setLastLoginTime(DateUtils.currentTime());
		user.setUpdated(DateUtils.currentTime());
		bgUserMapper.update(user);
		redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, user);
		
		HttpSession session = request.getSession();
		session.setAttribute("account", account);

		return Result.jsonSuccess(user);
	}
	
	public BgUser getBgUser(String account){
		BgUser bgUser = redisOperate.hgetBean(BgkeyGenerator.bgUserDataKey(), account, BgUser.class);
		if(bgUser==null){
			bgUser = bgUserMapper.find(account);
			if(null != bgUser)
				redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, bgUser);
		}
		return bgUser;
	}

	/**
	 * 修改密码
	 * @param account
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public String updatePwd(String account, String oldPwd, String newPwd) {
		BgUser user = getBgUser(account);
		if(user == null)
			throw IllegalConstException.errorException(JiLuParams.ACCOUNT);
		if(!user.getPassword().equals(IrisSecurity.toMd5(oldPwd)))
			throw IllegalConstException.errorException(JiLuParams.OLDPWD);
		user.setPassword(IrisSecurity.toMd5(newPwd));
		user.setUpdated(DateUtils.currentTime());
		bgUserMapper.update(user);
		redisOperate.hsetByJson(BgkeyGenerator.bgUserDataKey(), account, user);
		return Result.jsonSuccess(user);
	}

}
