package com.busap.vcs.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.data.mapper.HotLabelDAO;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.HotLabel;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.HotLabelRepository;
import com.busap.vcs.service.HotLabelService;
import com.busap.vcs.service.JedisService;

@Service("hotLabelService")
public class HotLabelServiceImpl extends BaseServiceImpl<HotLabel, Long> implements HotLabelService {
	
    
    
    @Resource(name = "hotLabelRepository")
    private HotLabelRepository hotLabelRepository;

	@Resource
	private HotLabelDAO hotLabelDAO;
	
	@Resource(name = "jedisService")
	private JedisService jedisService;
    
    
    @Resource(name = "hotLabelRepository")
    @Override
    public void setBaseRepository(BaseRepository<HotLabel, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }



	@Override
	public List<HotLabel> findAllOrderByDisplayorder() {
		Order order = new Order(Direction.DESC,"displayOrder");
		Sort sort = new Sort(order);
		String lableCountString = jedisService.get(BicycleConstants.HOT_LABLE_COUNT);
		int lableCount = 20;
		if (lableCountString != null && !"".equals(lableCountString)) {
			lableCount = Integer.parseInt(lableCountString);
		}
		return this.findAll(0, lableCount, null,sort);
	}  
    
	
	public List<HotLabel> find4ByShowOrder(){
		Order order = new Order(Direction.DESC,"showOrder");
		Sort sort = new Sort(order);
		return this.findAll(0, 4, null,sort);
	}

/*
	@Override
	public Integer queryHotLabelsCount(Map<String,Object> params){
		return hotLabelDAO.selectHotLabelsCount(params);
	}
*/

	@Override
	public List<HotLabel> queryHotLabels(Map<String,Object> params){
		return hotLabelDAO.selectHotLabels(params);
	}

	@Override
	public int insert(HotLabel hotLabel){
		return hotLabelDAO.insert(hotLabel);
	}

}
