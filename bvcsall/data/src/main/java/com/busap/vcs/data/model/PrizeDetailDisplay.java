package com.busap.vcs.data.model;

import com.busap.vcs.data.entity.PrizeDetail;

/**
 * Created by huoshanwei on 2015/10/26.
 */
public class PrizeDetailDisplay extends PrizeDetail {

    private String name;

    private String phone;

    private String username;
    
    private Integer vstat;

    //中奖用户头像
    private String userPic;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

	public Integer getVstat() {
		return vstat;
	}

	public void setVstat(Integer vstat) {
		this.vstat = vstat;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
