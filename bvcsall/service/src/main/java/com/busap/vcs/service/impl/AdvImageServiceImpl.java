package com.busap.vcs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.AdvImage;
import com.busap.vcs.data.repository.AdvImageRepository;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.service.AdvImageService;

@Service("advImageService")
public class AdvImageServiceImpl extends BaseServiceImpl<AdvImage, Long> implements AdvImageService{
	
    @Resource(name = "advImageRepository")
    private AdvImageRepository advImageRepository;
    
    @Resource(name = "advImageRepository")
    @Override
    public void setBaseRepository(BaseRepository<AdvImage, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }

	@Override
	public AdvImage findOne(long id) {
		return this.find(id);
	}

	@Override
	public List<AdvImage> findAdvImages() {
		return this.findAll();
	}
}
