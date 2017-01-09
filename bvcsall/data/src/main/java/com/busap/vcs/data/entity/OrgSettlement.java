package com.busap.vcs.data.entity;
/**
 * 结构结算
 */

import javax.persistence.Entity;
import javax.persistence.Table;
import com.busap.vcs.data.entity.BaseEntity;

@Entity
@Table(name = "org_settlement")
public class OrgSettlement extends BaseEntity {


	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -303944714671560849L;

	private Long userId;		//用户编号
	
	private Long orgId;		    //机构编号
	
	private Integer amount;		//价格(分)
	
	private Integer points;		//金豆
	
	private Integer realAmount;	//真实兑换价格(分)
	
	private Integer realPoints;		//真实兑换金豆
	
	private Integer status;		//状态0：未结算   1：已结算

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Integer realAmount) {
		this.realAmount = realAmount;
	}

	public Integer getRealPoints() {
		return realPoints;
	}

	public void setRealPoints(Integer realPoints) {
		this.realPoints = realPoints;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
}
