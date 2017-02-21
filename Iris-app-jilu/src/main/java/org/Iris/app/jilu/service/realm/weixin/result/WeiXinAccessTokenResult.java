package org.Iris.app.jilu.service.realm.weixin.result;
/**
 * 调用微信access_token返回实体类
 * @author 樊水东
 * 2017年2月14日
 */
public class WeiXinAccessTokenResult {
	/**
	 * 接口调用凭证
	 */
	private String access_token;
	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private String expires_in;
	/**
	 * 用户刷新access_token
	 */
	private String refresh_token;
	/**
	 * 授权用户唯一标识
	 */
	private String openid;
	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;
	/**
	 * 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
	 */
	private String unionid;
	/**
	 * 当接口调用失败时返回的错误码
	 */
	private int errcode;
	/**
	 * 错误码对应的错误信息
	 */
	private String errmsg;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	
}