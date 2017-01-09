package com.busap.vcs.oauth.third.qq;

import com.qq.connect.QQConnectException;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class MyQQUserInfoBean extends UserInfoBean {
	private String qq_pic;

	public String getQq_pic() {
		return qq_pic;
	}

	public void setQq_pic(String qq_pic) {
		this.qq_pic = qq_pic;
	}

	public MyQQUserInfoBean(JSONObject json) throws QQConnectException {
		super(json);
		try {
			setQq_pic(json.getString("figureurl_qq_2"));
		} catch (JSONException e) {
			setQq_pic(getAvatar().getAvatarURL100());
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1101179715037526000L;

}
