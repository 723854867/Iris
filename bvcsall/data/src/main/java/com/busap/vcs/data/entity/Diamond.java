package com.busap.vcs.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by busap on 2015/12/22.
 */
@Entity
@Table(name = "diamond")
public class Diamond extends BaseEntity {

    private static final long serialVersionUID = -649698129772735114L;

    @Column(name = "name",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String name;

    @Column(name = "diamond_icon_url",columnDefinition = "varchar(255) NOT NULL",nullable=false)
    private String diamondIconUrl; //拍币icon url

    @Column(name = "price",columnDefinition = "decimal(10,2) NOT NULL",nullable=false)
    private BigDecimal price;//拍币价格（人民币）

    @Column(name = "pay_method",columnDefinition = "tinyint(2) NOT NULL",nullable=false)
    private Integer payMethod;//后台配置允许支付方式 0为苹果支付 1为微信&支付宝2为h5大额支付 3为应用宝支付

    @Column(name = "diamond_count",columnDefinition = "int(8) NOT NULL",nullable=false)
    private Integer diamondCount;

    @Column(name = "is_give",columnDefinition = "tinyint(1) NOT NULL DEFAULT 0",nullable=false)
    private Integer isGive; //是否赠送 0不赠送 1赠送 默认为0

    @Column(name = "give_count",columnDefinition = "int(8) NOT NULL DEFAULT 0",nullable=true)
    private Integer giveCount;

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

    public String getDiamondIconUrl() {
        return diamondIconUrl;
    }

    public void setDiamondIconUrl(String diamondIconUrl) {
        this.diamondIconUrl = diamondIconUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getDiamondCount() {
        return diamondCount;
    }

    public void setDiamondCount(Integer diamondCount) {
        this.diamondCount = diamondCount;
    }

    public Integer getIsGive() {
        return isGive;
    }

    public void setIsGive(Integer isGive) {
        this.isGive = isGive;
    }

    public Integer getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(Integer giveCount) {
        this.giveCount = giveCount;
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
