package com.busap.vcs.data.model;

import java.util.Date;

/**
 * 用户重置明细
 * Created by Knight on 16/1/8.
 */
public class UserChargeDetail {

    private Date createTime;

    private String transactionNo;

    private Long userId;

    private String name;

    private String phone;

    private String channel;

    private float amount;
    
    private Double extra;
    
    private Double extraMoney;

    private String status;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Double getExtra() {
		return extra;
	}

    public void setExtra(Double extra) {
		this.extra = extra;
	}


	public Double getExtraMoney() {
		return extraMoney;
	}

	public void setExtraMoney(Double extraMoney) {
		this.extraMoney = extraMoney;
	}

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
