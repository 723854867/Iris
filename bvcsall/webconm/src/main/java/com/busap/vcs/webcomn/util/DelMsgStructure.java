package com.busap.vcs.webcomn.util;

/**
 * 删除消息参数结构
 * Created by Knight on 15/9/21.
 */
public class DelMsgStructure {

    private String type;
    private String id;

    DelMsgStructure() {
    }

    DelMsgStructure(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}