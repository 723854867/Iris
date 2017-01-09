package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 组织机构
 * Created by huoshanwei on 2016/3/22.
 */
@Entity
@Table(name = "organization")
public class Organization extends BaseEntity {

    private static final long serialVersionUID = 132346312425475205L;

    @Column(name = "org_name",columnDefinition = "varchar(200) NOT NULL",nullable=false)
    private String orgName;

    @Column(name = "leader",columnDefinition = "varchar(50) NOT NULL",nullable=false)
    private String leader;

    @Column(name = "leader_phone",columnDefinition = "varchar(11) NOT NULL",nullable=false)
    private String leaderPhone;

    @Column(name = "anchor_count",columnDefinition = "int(4) NOT NULL",nullable=false)
    private Integer anchorCount;
    
    @Column(name = "org_type",columnDefinition = "varchar(11) NOT NULL",nullable=false)
    private String orgType;//机构类型

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeaderPhone() {
        return leaderPhone;
    }

    public void setLeaderPhone(String leaderPhone) {
        this.leaderPhone = leaderPhone;
    }

    public Integer getAnchorCount() {
        return anchorCount;
    }

    public void setAnchorCount(Integer anchorCount) {
        this.anchorCount = anchorCount;
    }

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
    
    
}
