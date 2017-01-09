package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.InviteInfo;
import com.busap.vcs.data.mapper.InviteInfoDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.InviteInfoRepository;
import com.busap.vcs.data.vo.InviteInfoVO;
import com.busap.vcs.service.InviteInfoService;

@Service("inviteInfoService")
public class InviteInfoServiceImpl extends BaseServiceImpl<InviteInfo,Long> implements InviteInfoService {
	
	
    @Resource(name = "inviteInfoRepository")
    private InviteInfoRepository inviteInfoRepository;
	
    
    @Resource(name = "inviteInfoRepository")
    @Override
    public void setBaseRepository(BaseRepository<InviteInfo, Long> inviteInfoRepository) {
        super.setBaseRepository(inviteInfoRepository);
    }
	@Autowired
	InviteInfoDAO inviteInfoDao;
	
	@Override
	public List<InviteInfoVO> findAllInviteInfo() {
		return inviteInfoDao.findAllInviteInfo();
	}

	@Override
	public List<InviteInfoVO> findInviteInfoByPlatformMark(String platformMark) {
		return inviteInfoDao.findInviteInfoByPlatformMark(platformMark);
	}
	
	@Override
	public List<InviteInfoVO> findAvaliblelInviteInfo() {
		return inviteInfoDao.findAvaliblelInviteInfo();
	}
	
}
