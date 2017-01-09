package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 功能 兑换/提现 基本信息
 * Created by huoshanwei on 2016/4/6.
 */
@Entity
@Table(name = "exchange")
public class Exchange extends BaseEntity {

    private static final long serialVersionUID = 1837537662701741148L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name; //名称

    @Column(name = "icon_url",columnDefinition = "varchar(255) NULL",nullable=true)
    private String iconUrl; //icon url

    @Column(name = "point_count",columnDefinition = "int(8) NOT NULL",nullable=false)
    private Integer pointCount; //需要兑换的金豆数量

    @Column(name = "diamond_count",columnDefinition = "int(8) NULL",nullable=true)
    private Integer diamondCount; //兑换金币的数量 通过计算得到

    @Column(name = "price",columnDefinition = "decimal(10,2) NULL",nullable=true)
    private BigDecimal price; //兑换人民币数量 通过计算得到 元

    @Column(name = "type",columnDefinition = "tinyint(1) NOT NULL",nullable=false)
    private Integer type;//类型 1兑换 2提现

    @Column(name = "state",columnDefinition = "tinyint(1) NOT NULL DEFAULT 1",nullable=false)
    private Integer state;//状态 1上架 0下架 默认为1

    @Column(name = "weight",columnDefinition = "double(6,2) NOT NULL DEFAULT 0.00",nullable=false)
    private Double weight;//权重

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getPointCount() {
        return pointCount;
    }

    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

    public Integer getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(Integer diamondCount) {
        this.diamondCount = diamondCount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
