package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.busap.vcs.data.enums.DataFrom;
import com.busap.vcs.data.enums.TagStatus;

@Entity
@Table(name = "tag")
public class Tag extends BaseEntity{
	 
	private static final long serialVersionUID = -6779472947860259775L;

	private String name;
	
	private String status=TagStatus.活动.getName();
	
//	@Column(nullable=false,length=32)
//    private String dataFrom=DataFrom.移动麦视后台.getName();
	
	private int orderNum;

	public Tag(){
		this.dataFrom=DataFrom.移动麦视后台.getName();
	}
	
//	public String getDataFrom() {
//		return dataFrom;
//	}
//
//	public void setDataFrom(String dataFrom) {
//		this.dataFrom = dataFrom;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	
	
}
