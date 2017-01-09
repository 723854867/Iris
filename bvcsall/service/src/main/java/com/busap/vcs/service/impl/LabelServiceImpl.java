package com.busap.vcs.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.mapper.LabelDAO;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.busap.vcs.base.Filter;
import com.busap.vcs.data.entity.Label;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.HotLabelRepository;
import com.busap.vcs.data.repository.LabelRepository;
import com.busap.vcs.data.repository.LabelVideoRepository;
import com.busap.vcs.service.LabelService;

@Service("labelService")
public class LabelServiceImpl extends BaseServiceImpl<Label, Long> implements LabelService {
	
    @Resource(name = "labelRepository")
    private LabelRepository labelRepository;
    
    @Resource(name = "labelVideoRepository")
    private LabelVideoRepository labelVideoRepository;
    
    @Resource(name = "hotLabelRepository")

    private HotLabelRepository hotLabelRepository;

    @Resource
    private LabelDAO labelDAO;

    @Resource(name = "labelRepository")
    @Override
    public void setBaseRepository(BaseRepository<Label, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public List<Label> findMatchTag(String tag) {
		Order order = new Order(Direction.DESC,"num");
		Sort sort = new Sort(order);
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(Filter.like("name", tag+"%"));
		return this.findAll(0, 20, filters, sort);
	}

    @Override
    public List<Label> queryLabels(Map<String,Object> params){
        return labelDAO.selectLabels(params);
    }

    @Override
    public Integer queryLabelsCount(Map<String,Object> params){
        return labelDAO.selectLabelsCount(params);
    }
    

	

}
