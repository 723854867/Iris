package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.mapper.SingerMapper;
import com.busap.vcs.service.SingerService;

/**
 *
 * Created by Knight on 16/5/5.
 */
@Service("singerService")
public class SingerServiceImpl implements SingerService {

    @Autowired
    private SingerMapper singerMapper;

	@Override
	public List<Map> querySinger(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return singerMapper.searchSinger(params);
		 
	}

	@Override
	public List<Map> searchRicher(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return singerMapper.searchRicher(params);
	}

   
}
