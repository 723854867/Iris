package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.mapper.CoverDAO;
import com.busap.vcs.data.vo.CoverVO;
import com.busap.vcs.service.CoverService;

@Service("coverService")
public class CoverServiceImpl implements CoverService {

	@Autowired
	CoverDAO coverDao;

	@Override
	public CoverVO getRandomActiveCover() {
		List<CoverVO> list = coverDao.selectActiveCovers();
		if (list != null && list.size() > 0) {
			Random random = new Random();
			int index = random.nextInt(list.size());
			return list.get(index);
		}
		return null;
	}
	
}
