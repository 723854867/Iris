package com.busap.vcs.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.busap.vcs.data.entity.Complain;
import com.busap.vcs.data.entity.Version;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.VersionRepository;
import com.busap.vcs.service.VersionService;

@Service("versionService")
public class VersionServiceImpl extends BaseServiceImpl<Version, Long> implements VersionService {
	
    @Resource(name = "versionRepository")
    private VersionRepository versionRepository;

    @Resource(name = "versionRepository")
    @Override
    public void setBaseRepository(BaseRepository<Version, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }  
     
    

}
