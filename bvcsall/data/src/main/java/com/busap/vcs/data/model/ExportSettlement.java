package com.busap.vcs.data.model;

import java.util.Date;

/**
 * Created by huoshanwei on 2015/12/30.
 */
public class ExportSettlement {

	private String userId;		//用户id
	private String name;		//用户昵称
	private String username;	//用户名
	private String phone;		//手机号
    private String points;		//结算点数
    private Date createDate;	//创建时间
    private String creator;		//创建人
    private Date approveTime;	//审核时间
    private String approver;	//审核人
    private Date settleTime;	//结算时间
    private String settler;		//结算人
    private String status;		//结算状态

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}

	public String getSettler() {
		return settler;
	}

	public void setSettler(String settler) {
		this.settler = settler;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
