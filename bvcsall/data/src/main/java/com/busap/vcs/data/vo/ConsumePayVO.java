package com.busap.vcs.data.vo;

import com.busap.vcs.data.entity.OrderPay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 用户充值明细查询
 * Created by Knight on 16/1/6.
 */
public class ConsumePayVO extends OrderPay {

    private String username;
    private String phone;
    private String time;
    private Double extraMoney;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public Double getExtraMoney() {
		return extraMoney;
	}

    public void setExtraMoney(Double extraMoney) {
		this.extraMoney = extraMoney;
	}

	public void setTime(long createTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(createTime * 1000);
        Date date = c.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = format.format(date);
    }

}
