package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="sign_user")
public class SignUser extends BaseEntity {

	/**积分记录表
	 * 
	 */
	private static final long serialVersionUID = -2073359218968158690L;
	@Column(name = "sum_sign_num")
	private Integer sumSignNum;//获得积分数量
	@Column(name = "continue_sign")
	private Integer continueSign;//连续获得积分天数
	
	
	public Integer getSumSignNum() {
		return sumSignNum;
	}
	public void setSumSignNum(Integer sumSignNum) {
		this.sumSignNum = sumSignNum;
	}
	public Integer getContinueSign() {
		return continueSign;
	}
	public void setContinueSign(Integer continueSign) {
		this.continueSign = continueSign;
	}


	
}
