package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by huoshanwei on 2015/10/23.
 */
@Entity
@Table(name = "prize_detail")
public class PrizeDetail extends BaseEntity {

    //活动奖项ID
    @Column(name = "prize_id",columnDefinition = "bigint(8) NULL",nullable=false)
    private Long prizeId;

    //中奖用户ID
    @Column(name = "user_id",columnDefinition = "bigint(20) NULL",nullable=false)
    private Long userId;

    //奖项名次
    //@Column(name = "prize_level",columnDefinition = "int(4) NULL",nullable=false)
    private Long prizeLevel;

    //奖项名称
    @Column(name = "prize_level_name",columnDefinition = "varchar(50) NULL",nullable=false)
    private String prizeLevelName;

    //奖品名称
    private String prizeName;

    public Long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Long prizeId) {
        this.prizeId = prizeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPrizeLevel() {
        return prizeLevel;
    }

    public void setPrizeLevel(Long prizeLevel) {
        this.prizeLevel = prizeLevel;
    }

    public String getPrizeLevelName() {
        return prizeLevelName;
    }

    public void setPrizeLevelName(String prizeLevelName) {
        this.prizeLevelName = prizeLevelName;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}
