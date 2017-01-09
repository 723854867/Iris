package com.busap.vcs.data.vo;

import java.util.Date;


public class PraiseUserVO {

	private Long id;//用户id
	private String name;//用户昵称
	private String pic;//用户头像 
    private String signature;//个性签名
	private Long praiseId;//赞id
	private Date praiseDate;//赞时间
	private boolean attention=false;//是否被当前登录用户关注 

	
	public Date getPraiseDate() {
		return praiseDate;
	}
	public void setPraiseDate(Date praiseDate) {
		this.praiseDate = praiseDate;
	}
	public boolean isAttention() {
		return attention;
	}
	public void setAttention(boolean attention) {
		this.attention = attention;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public Long getPraiseId() {
		return praiseId;
	}
	public void setPraiseId(Long praiseId) {
		this.praiseId = praiseId;
	} 
}
