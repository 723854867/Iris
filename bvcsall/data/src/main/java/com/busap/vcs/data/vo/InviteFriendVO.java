package com.busap.vcs.data.vo;

public class InviteFriendVO {
	
	private Long id = 0l;  //我拍用户uid
	
	private Integer isWopaiUser = 0;  //是否为我拍用户,1:是，0：不是
	
	private Integer isAttention = 0;  //是否关注了该用户，1：是，0：否
	
    private String wopaiNickname;  //我拍的用户昵称
    
    private String wopaiPic;  //我拍头像地址
    
    private String thirdPartNickname;  //第三方昵称,如果是通讯录的话，为通讯录中的姓名
    
    private String thirdPartPic;  //第三方头像
    
    private String type;  //我拍用户类型（普通用户、马甲等）
    
    private Integer vipStat = -1;  //我拍vip级别
    
    private String dataFrom;  //"contacts":通讯录，"sina":新浪微博;
    
    private String username;  //我拍登陆名，手机号注册用户的手机号码
    
    private String thirdUsername;  //第三方用户唯一标识,如果是通讯录的话，为通讯录中手机号码


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIsWopaiUser() {
		return isWopaiUser;
	}

	public void setIsWopaiUser(Integer isWopaiUser) {
		this.isWopaiUser = isWopaiUser;
	}

	public Integer getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(Integer isAttention) {
		this.isAttention = isAttention;
	}

	public String getWopaiNickname() {
		return wopaiNickname;
	}

	public void setWopaiNickname(String wopaiNickname) {
		this.wopaiNickname = wopaiNickname;
	}

	public String getWopaiPic() {
		return wopaiPic;
	}

	public void setWopaiPic(String wopaiPic) {
		this.wopaiPic = wopaiPic;
	}

	public String getThirdPartNickname() {
		return thirdPartNickname;
	}

	public void setThirdPartNickname(String thirdPartNickname) {
		this.thirdPartNickname = thirdPartNickname;
	}

	public String getThirdPartPic() {
		return thirdPartPic;
	}

	public void setThirdPartPic(String thirdPartPic) {
		this.thirdPartPic = thirdPartPic;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getVipStat() {
		return vipStat;
	}

	public void setVipStat(Integer vipStat) {
		this.vipStat = vipStat;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getThirdUsername() {
		return thirdUsername;
	}

	public void setThirdUsername(String thirdUsername) {
		this.thirdUsername = thirdUsername;
	}

}
