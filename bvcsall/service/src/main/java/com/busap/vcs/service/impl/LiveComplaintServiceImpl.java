package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Complain;
import com.busap.vcs.data.entity.LiveComplaint;
import com.busap.vcs.data.mapper.ComplainDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ComplainRepository;
import com.busap.vcs.data.repository.LiveComplaintRepository;
import com.busap.vcs.data.vo.ComplainVO;
import com.busap.vcs.service.ComplainService;
import com.busap.vcs.service.LiveComplaintService;
import com.busap.vcs.service.RoomService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;

@Service("liveComplaintService")
public class LiveComplaintServiceImpl extends BaseServiceImpl<LiveComplaint, Long> implements LiveComplaintService {
	
    @Resource(name = "liveComplaintRepository")
    private LiveComplaintRepository liveComplaintRepository;
    
//    @Autowired
//    private LiveComplaintDAO liveComplaintDao;

    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "videoService")
	private VideoService videoService;
    
    @Resource(name = "roomService")
	private RoomService roomService;
     
    @Resource(name = "liveComplaintRepository")
    @Override
    public void setBaseRepository(BaseRepository<LiveComplaint, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

    public Long unDealComplaintsCount() {
    	return liveComplaintRepository.unDealComplaintsCount();
    }
    

}
