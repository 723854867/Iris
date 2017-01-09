package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

//用户订阅
@Entity
@Table(name = "subscribe")
public class Subscribe extends BaseEntity{  
	 
	private static final long serialVersionUID = -142968579938117098L;

	private Long  activityId;

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}    
	
	
	
}
