package com.busap.vcs.data.entity;
/**
 * 结算清单
 */
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "settlement")
public class Settlement extends BaseEntity {
	private static final long serialVersionUID = 5679330448870755308L;

	private Integer points; 	//结算点数
	
	private Integer rmb;		//应结算人民币数
	
	private Long reciever;		//主播id
	
	private Integer status = 0;	//状态，0 新申请、1 已审核、2 已结算
	
	private Long approverId;	//审核人ID
	
	private Long settlerId;		//结算人ID
	
	private Date approveTime;	//审核时间
	
	private Date settleTime;	//结算时间
	
	private String extr;		//备注
	

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Long getReciever() {
		return reciever;
	}

	public void setReciever(Long reciever) {
		this.reciever = reciever;
	}

	public Integer getRmb() {
		return rmb;
	}

	public void setRmb(Integer rmb) {
		this.rmb = rmb;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public Long getSettlerId() {
		return settlerId;
	}

	public void setSettlerId(Long settlerId) {
		this.settlerId = settlerId;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}

	public String getExtr() {
		return extr;
	}

	public void setExtr(String extr) {
		this.extr = extr;
	}
	
	
	
}
