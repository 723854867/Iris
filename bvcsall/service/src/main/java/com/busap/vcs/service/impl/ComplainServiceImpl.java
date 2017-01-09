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
import com.busap.vcs.data.mapper.ComplainDAO;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ComplainRepository;
import com.busap.vcs.data.vo.ComplainVO;
import com.busap.vcs.service.ComplainService;
import com.busap.vcs.service.RuserService;
import com.busap.vcs.service.VideoService;

@Service("complainService")
public class ComplainServiceImpl extends BaseServiceImpl<Complain, Long> implements ComplainService {
	
    @Resource(name = "complainRepository")
    private ComplainRepository complainRepository;
    
    @Autowired
    private ComplainDAO complainDao;

    @Resource(name = "ruserService")
	private RuserService ruserService;
    
    @Resource(name = "videoService")
	private VideoService videoService;
     
    @Resource(name = "complainRepository")
    @Override
    public void setBaseRepository(BaseRepository<Complain, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public Page<ComplainVO> listpage(int page, int size, Map<String,Object> params) {
		List<ComplainVO> voList = complainDao.searchComplain(params);
		Integer total = complainDao.searchComplainCount(params);
		Page<ComplainVO> pinfo = new PageImpl<ComplainVO>(voList,new PageRequest(page-1, size, null),total);

		return pinfo;
	}
     
    

}
