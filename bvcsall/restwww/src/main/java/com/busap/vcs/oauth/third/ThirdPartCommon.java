package com.busap.vcs.oauth.third;

import java.security.InvalidParameterException;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.busap.vcs.data.entity.Ruser;
import com.busap.vcs.data.entity.Ruser.ThirdUser;
import com.busap.vcs.oauth.third.weibo.weibo4j.org.json.JSONException;
import com.busap.vcs.oauth.web.exception.ThirdPartException;

/***
 * 1、第三方返回信息有效性验证[令牌]<br/>
 * 2、第三方账户信息获取<br/>
 * 3、令牌生成和存储
 * 
 * @author shouchen.shan@1020.cn
 * 
 */
public interface ThirdPartCommon {

	/**
	 * 验证当前令牌(tokenId)是否有效
	 * @param access_token 令牌
	 * @param uid 用户Id
	 * @return
	 * @throws InvalidParameterException
	 * @throws JSONException
	 * @throws ThirdPartException
	 */
	boolean validate(String access_token, String uid)
			throws InvalidParameterException, ThirdPartException, JSONException;

	/**
	 * 根据有效的令牌(TokenId),从第三方账户获取用户信息
	 * 
	 * @param access_token
	 *            令牌
	 * @param uid
	 *            用户ID
	 * @return
	 */
	Ruser getInfo(String access_token, String uid);
	
	public String getUnionid(String access_token, String uid);

	/**
	 * 生成&存储令牌
	 * 
	 * @param uid
	 *            用户Id
	 * @param expire
	 *            超时时间
	 * @return
	 * @throws OAuthSystemException
	 */
	String createAndStoreAccessToken(String uid, String expire)
			throws OAuthSystemException;
	/**
	 * 判断账号是否已经注册
	 * @param uid
	 * @return
	 */
	boolean isExist(String uid);
	
	/**
	 * 第三方登录的名称<br>
	 * @see ThirdUser
	 * @return
	 */
	String thirdPartName();
}
