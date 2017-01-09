package com.busap.vcs.service.impl;


import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.busap.vcs.data.entity.LabelVideo;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LabelVideoRepository;
import com.busap.vcs.service.LabelVideoService;

@Service("labelVideoService")
public class LabelVideoServiceImpl extends BaseServiceImpl<LabelVideo, Long> implements LabelVideoService {
	
    
    @Resource(name = "labelVideoRepository")
    private LabelVideoRepository labelVideoRepository;
    
     
    
    
    @Resource(name = "labelRepository")
    @Override
    public void setBaseRepository(BaseRepository<LabelVideo, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
    

	

}
