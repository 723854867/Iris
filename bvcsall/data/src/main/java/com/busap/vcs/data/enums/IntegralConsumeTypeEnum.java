package com.busap.vcs.data.enums;

/**
 * 消费类型
 * Created by Knight on 15/10/21.
 */
public enum  IntegralConsumeTypeEnum {
    //兑换类型：alipay(支付宝), qb(Q币), coupon(优惠券), object(实物), phonebill(话费), phoneFlow(流量), virtual(虚拟商品), turntable(大转盘), singleLottery(单品抽奖) 所有类型不区分大小写
    alipay(101, "alipay", "支付宝"),
    qb(102, "qb", "Q币"),
    coupon(103, "coupon", "优惠券"),
    object(104, "object", "实物"),
    phonebill(105, "phonebill", "话费"),
    phoneflow(106, "phoneflow", "流量"),
    virtual(107, "virtual", "虚拟商品"),
    turntable(108, "turntable", "大转盘"),
    singleLottery(109, "singleLottery", "单品抽奖"),;


    private int type;

    private String typeString;

    private String description;

    IntegralConsumeTypeEnum(int type, String typeString, String description) {
        this.type = type;
        this.typeString = typeString;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static int getValueByTypeString(String typeString) {
        for (IntegralConsumeTypeEnum type : IntegralConsumeTypeEnum.values()) {
            if (type.getTypeString().equals(typeString)) {
                return type.getType();
            }
        }
        return 0;
    }
}
