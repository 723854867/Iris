package org.Iris.app.jilu.common.bean.model;

/**
 * 好友列表模板
 * @author 樊水东
 * 2017年2月16日
 */
public class FriendListModel {

	//好友id
	private long friendId;
	//好友名字
	private String friendName;
	private String mobile;
	private String address;
	//好友云信账号
	private String accid;
	//好友云信密码
	private String token;
	
	private int created;
	
	public FriendListModel(){
		
	}

	public long getFriendId() {
		return friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public String getAccid() {
		return accid;
	}

	public void setAccid(String accid) {
		this.accid = accid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
