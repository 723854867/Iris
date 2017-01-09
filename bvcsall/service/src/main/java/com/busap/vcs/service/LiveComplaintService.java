package com.busap.vcs.service;

import com.busap.vcs.data.entity.LiveComplaint;

public interface LiveComplaintService extends BaseService<LiveComplaint, Long> {
	
	public Long unDealComplaintsCount();
 
}
