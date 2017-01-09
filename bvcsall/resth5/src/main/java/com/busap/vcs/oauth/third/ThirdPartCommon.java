package com.busap.vcs.oauth.third;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import com.busap.vcs.data.entity.Ruser.ThirdUser;

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
	
	String getOldToken(String key);
}
