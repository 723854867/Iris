package com.busap.vcs.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "activity_video")
public class ActivityVideo extends BaseEntity{  
 
	private static final long serialVersionUID = -6303187147243052993L;
	
//	@Column(  insertable=false,updatable=false)
	private long videoid; 
	
//	@Column(  insertable=false,updatable=false)
	private long activityid; 
	private int  orderNum; 
	
//	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)     
//	@JoinColumn(name = "activityid" )
	@Transient
	private Activity activity;
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public long getVideoid() {
		return videoid;
	}
	public void setVideoid(long videoid) {
		this.videoid = videoid;
	}
	public long getActivityid() {
		return activityid;
	}
	public void setActivityid(long activityid) {
		this.activityid = activityid;
	} 
	 
	
	 
	
}
