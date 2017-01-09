package com.busap.vcs.data.entity;


public class LiveParam extends BaseEntity {

    // 提示类型
    private Integer type;

    // 描述
    private String description;

    // 阀值
    private Integer count;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}