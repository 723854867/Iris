package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Sign;
import com.busap.vcs.data.mapper.SignDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.SignRepository;
import com.busap.vcs.data.vo.SignVO;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.SignService;
@Service("signService")
public class SignServiceImpl extends BaseServiceImpl<Sign,Long> implements SignService  {
    @Resource(name = "signRepository")
    private SignRepository signRepository;
    
    @Resource(name = "signRepository")
    @Override
    public void setBaseRepository(BaseRepository<Sign, Long> signRepository) {
        super.setBaseRepository(signRepository);
    }
    
	@Autowired
    JedisService jedisService;
	
	@Autowired
	SignDAO signDao;
	
	@Override
	public List<SignVO> findUserAllSgin(String uid,int pageStart,int pageSize) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("uid", uid);
		params.put("pageStart", pageStart);
		params.put("pageSize", pageSize);
		return signDao.findUserAllSgin(params);
	}
	
	//获取用户平台所有url配置
	@Override
	public List<SignVO> findEveryDaySumSign(int pageStart,int pageSize,String startTime,String endTime,Integer startCount,Integer endCount) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("pageStart", (pageStart-1)*pageSize);
		params.put("pageSize", pageSize);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("startCount", startCount);
		params.put("endCount", endCount);
		return signDao.findEveryDaySumSign(params);
	}
	//获取用户平台所有url配置
	@Override
	public Integer findEveryDaySumSignCount(String startTime,String endTime,Integer startCount,Integer endCount) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("startCount", startCount);
		params.put("endCount", endCount);
		return signDao.findEveryDaySumSignCount(params);
	}
	@Override
	public List<SignVO> findYesterdaySign(String uid){
		return signDao.findYesterdaySign(uid);
	}
	@Override
	public List<SignVO> findTodaySign(String uid){
		return signDao.findTodaySign(uid);
	}

	@Override
	public Integer findAllBeyondMaxUser() {
		return signDao.findAllBeyondMaxUser();
	}

	@Override
	public Integer findAllSignUser() {
		return signDao.findAllSignUser();
	}

	@Override
	public List<SignVO> findAllSgin(String uid, int pageStart, int pageSize) {
		return null;
	}

	@Override
	public Integer findSginToday() {
		return null;
	}

	@Override
	public List<SignVO> findPraiseShareSign(String videoId, String fromUid,
			String uid) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("videoId", videoId);
		params.put("uid", uid);
		params.put("fromUid", fromUid);
		return signDao.findPraiseShareSign(params);
	}

}
