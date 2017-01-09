package com.busap.vcs.data.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by busap on 2015/12/22.
 */
@Entity
@Table(name = "dia_mon_remain")
public class DiamondMonthRemaining extends BaseEntity {



    /**
	 * 
	 */
	private static final long serialVersionUID = 5951154616210802100L;


	@Column(name = "price",columnDefinition = "decimal(20,2) NOT NULL",nullable=false)
    private BigDecimal price;//拍币价格（人民币）


    @Column(name = "diamond_count",columnDefinition = "bigint(20) NOT NULL",nullable=false)
    private BigInteger diamondCount;

    @Column(name = "mon",nullable=false)
    private Date mon; //

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigInteger getDiamondCount() {
		return diamondCount;
	}

	public void setDiamondCount(BigInteger diamondCount) {
		this.diamondCount = diamondCount;
	}

	public Date getMon() {
		return mon;
	}

	public void setMon(Date mon) {
		this.mon = mon;
	}


    
    
}
