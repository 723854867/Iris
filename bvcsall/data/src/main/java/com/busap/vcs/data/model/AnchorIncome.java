package com.busap.vcs.data.model;


/**
 * Created by busap on 2015/12/29.
 */
public class AnchorIncome {

    private Long userId;

    private String userName;

    private String name;

    private String phone;

    private int pointCount = 0;
    
    private Integer lockPoints = 0;
	//已结算点数
	private Integer settledPoints = 0;
	
    public int getPointCount() {
        return pointCount;
    }

    public void setPointCount(int pointCount) {
        this.pointCount = pointCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public Integer getLockPoints() {
		return lockPoints;
	}

	public void setLockPoints(Integer lockPoints) {
		this.lockPoints = lockPoints;
	}

	public Integer getSettledPoints() {
		return settledPoints;
	}

	public void setSettledPoints(Integer settledPoints) {
		this.settledPoints = settledPoints;
	}
}
