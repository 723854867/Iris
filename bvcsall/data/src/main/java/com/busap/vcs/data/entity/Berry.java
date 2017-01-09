package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "berry")
public class Berry extends BaseEntity {

	private static final long serialVersionUID = 3713680417660604236L;

	private Long destId; //被种草莓用户id

	public Long getDestId() {
		return destId;
	}

	public void setDestId(Long destId) {
		this.destId = destId;
	}
	
}
