package com.busap.vcs.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.busap.vcs.data.entity.Probe;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ProbeRepository;
import com.busap.vcs.service.ProbeService;

@Service("probeService")
public class ProbeServiceImpl extends BaseServiceImpl<Probe, Long> implements ProbeService {
 
	
	@Resource(name = "probeRepository")
	private ProbeRepository probeRepository;
	 

	@Resource(name = "probeRepository")
	@Override
	public void setBaseRepository(BaseRepository<Probe, Long> baseRepository) {
		super.setBaseRepository(baseRepository);
	} 
	 
}
