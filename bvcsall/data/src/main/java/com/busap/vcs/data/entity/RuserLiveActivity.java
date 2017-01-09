package com.busap.vcs.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 用户参与直播活动关联表
 * @author Administrator
 *
 */
@Entity
@Table(name = "ruser_live_activity")
public class RuserLiveActivity extends BaseEntity{

	private static final long serialVersionUID = 5518579522070619646L;  
	private Long liveActivityId;  //参与的直播活动id
	public Long getLiveActivityId() {
		return liveActivityId;
	}
	public void setLiveActivityId(Long liveActivityId) {
		this.liveActivityId = liveActivityId;
	}
	
}
