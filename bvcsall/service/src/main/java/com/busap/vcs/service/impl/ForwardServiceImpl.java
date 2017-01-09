package com.busap.vcs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Forward;
import com.busap.vcs.data.mapper.ForwardDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ForwardRepository;
import com.busap.vcs.service.ForwardService;

@Service("forwardService")
public class ForwardServiceImpl extends BaseServiceImpl<Forward, Long> implements ForwardService {
	@Resource(name = "forwardRepository")
    private ForwardRepository forwardRepository;
	
	@Autowired
	private ForwardDAO forwardDAO;
     
    @Resource(name = "forwardRepository")
    @Override
    public void setBaseRepository(BaseRepository<Forward, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public boolean isForward(Long videoId, Long uid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("videoId", videoId);
		int isForward = forwardDAO.isForward(params);
		return isForward>0?true:false;
	}

	@Override
	public boolean hasForwarded(Long uid) {
		return forwardDAO.hasForwarded(uid) >0 ? true:false;
	}

	@Override
	public void cancelForward(Long videoId, Long uid) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("uid", uid);
		params.put("videoId", videoId);
		forwardDAO.cancelForward(params);
	}

	@Override
	public void deleteForwardByIds(List<Long> ids) {
		try {
			forwardDAO.deleteForward(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getForwardNumber(Long uid) {
		return forwardDAO.hasForwarded(uid);
	}

}
