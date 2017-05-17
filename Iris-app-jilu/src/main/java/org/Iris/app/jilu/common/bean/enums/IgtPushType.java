package org.Iris.app.jilu.common.bean.enums;
/**
 * 推送类型
 * @author 樊水东
 * 2017年2月10日
 */
public enum IgtPushType {

	/**
	 * 转单推送
	 */
	ORDER_TRANSFORM(0,"吉鹿消息","您收到一个转单请求"),
	/**
	 * 转单被取消
	 */
	ORDER_CANCEL(1,"吉鹿消息","您有一个转单请求已经被取消"),
	/**
	 * 转单被接受
	 */
	ORDER_RECEIVE(2,"吉鹿消息","您的一个转单请求已经被接受"),
	/**
	 * 转单被拒绝
	 */
	ORDER_REFUSE(3,"吉鹿消息","您的一个转单请求已经被拒绝"),
	/**
	 * 订单状态改变
	 */
	ORDER_STATUS_CHANGE(4,"吉鹿消息","您的订单{0}状态改变为{1}"),
	/**
	 * 好友申请
	 */
	FRIEND_APPLY(5,"吉鹿消息","{0}申请成为您的好友"),
	/**
	 * 好友申请处理
	 */
	FRIEND_APPLY_REPLY(6,"吉鹿消息","您发送给{0}的好友申请已经{1}"),
	/**
	 * 订单备注修改
	 */
	ORDER_MEMO_EDIT(7,"吉鹿消息","订单{0}的备注信息更新了"),
	/**
	 * 推送公告
	 */
	BANNER_PUBLISH(8,"吉鹿消息","有一条新公告发布");
	
	private int type;
	private String title;
	private String content;
	
	
	private IgtPushType(int type,String title,String content){
		this.type = type;
		this.title = title;
		this.content = content;
	}
	
	public int type(){
		return this.type;
	}

	public int getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
}
