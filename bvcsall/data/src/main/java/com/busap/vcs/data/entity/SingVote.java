package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 新歌声投票表
 *
 */
@Entity
@Table(name = "sing_vote")
public class SingVote extends BaseEntity {

	private static final long serialVersionUID = 5526940789337633082L;
	
	private Long destId; //被投票用户id
	private Integer type; //投票类型，1：CD投票，2：礼物投票，3：付费投票，4：人工干预投票
	private Long value; //投票值，CD投票和付费投票，每一票算1，礼物投票按金豆数的值算
	private Long popularity; //人气值: value*radio
	private Integer userType; //用户类型：1：学员，2：普通用户
	private Long operationId; //操作人Id

	public Long getDestId() {
		return destId;
	}
	public void setDestId(Long destId) {
		this.destId = destId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	public Long getPopularity() {
		return popularity;
	}
	public void setPopularity(Long popularity) {
		this.popularity = popularity;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Long getOperationId() {
		return operationId;
	}

	public void setOperationId(Long operationId) {
		this.operationId = operationId;
	}
}
