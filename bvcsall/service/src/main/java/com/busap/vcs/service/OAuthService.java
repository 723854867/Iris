package com.busap.vcs.service;

import java.util.Map;

public interface OAuthService {
	 //添加 auth code
    public void addAuthCode(String authCode, String username);
    //添加 access token
    public void addAccessToken(String accessToken, String username);
    /**
     * 添加token
     * @param accessToken
     * @param username
     * @param expire
     */
    public void addAccessToken(String accessToken,String username,String expire);

    //验证auth code是否有效
    boolean checkAuthCode(String authCode);
    //验证access token是否有效
    boolean checkAccessToken(String accessToken);

    String getUsernameByAuthCode(String authCode);
    String getUsernameByAccessToken(String accessToken);


    //auth code / access token 过期时间
    long getExpireIn();


    public boolean checkClientId(String clientId);

    public boolean checkClientSecret(String clientSecret);
    
    public Map<String,String> getAuthInfoByAuthCode(String authCode);
    
    public Map<String,String> getAccessTokenInfoByAccessToken(String accessToken);
    
    /**
     * 验证Token是否过期
     * @param accessToken
     * @return
     */
    public boolean isExpireAccessToken(String accessToken);
    
    public String getAccessTokenInfoByUsername(String username);
	public boolean checkAccessTokenAndUid(String accessToken, String uid);
    

}
