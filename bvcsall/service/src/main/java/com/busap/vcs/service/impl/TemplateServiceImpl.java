package com.busap.vcs.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.busap.vcs.data.mapper.TemplateMapper;
import com.busap.vcs.data.model.TemplateDisplay;
import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Template;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.TemplateRepository;
import com.busap.vcs.service.TemplateService;

@Service("templateService")
public class TemplateServiceImpl extends BaseServiceImpl<Template, Long> implements TemplateService{
	
    @Resource(name = "templateRepository")
    private TemplateRepository templateRepository;

	@Resource
	private TemplateMapper templateMapper;

    @Resource(name = "templateRepository")
    @Override
    public void setBaseRepository(BaseRepository<Template, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public Template findOne(long id) {
		return this.find(id);
	}

	@Override
	public List<Template> findTempaltes() {
		return this.findAll();
	}

	@Override
	public List<TemplateDisplay> queryTemplates(Map<String,Object> params){
		return templateMapper.selectTemplates(params);
	}

/*	@Override
	public Integer queryTemplateCount(Map<String,Object> params){
		return templateMapper.selectTemplateCount(params);
	}*/

}
