package com.busap.vcs.data.vo;

import java.util.Map;

/**
 * 应答报文对象
 * Created by Knight on 16/5/30.
 */
public class CustPicResponse {
    /**
     * 返回状态码
     * 0000:代表成功 1001:系统异常 2999:识别失败
     */
    private String returnCode;
    /**
     * 返回消息
     */
    private String returnMessage;
    /**
     * 返回身份证 信息
     */
    private Map<String, String> bean;

    private String timeStamp;

    private String[] beans;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public Map<String, String> getBean() {
        return bean;
    }

    public void setBean(Map<String, String> bean) {
        this.bean = bean;
    }

    public String[] getBeans() {
        return beans;
    }

    public void setBeans(String[] beans) {
        this.beans = beans;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
