package com.busap.vcs.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.busap.vcs.data.entity.LiveNotice;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.LiveNoticeRepository;
import com.busap.vcs.service.LiveNoticeService;

@Service("liveNoticeService")
public class LiveNoticeServiceImpl extends BaseServiceImpl<LiveNotice, Long> implements LiveNoticeService {
	
    @Resource(name = "liveNoticeRepository")
    private LiveNoticeRepository liveNoticeRepository;
    

     
    @Resource(name = "liveNoticeRepository")
    @Override
    public void setBaseRepository(BaseRepository<LiveNotice, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }



	@Override
	public List<LiveNotice> findLiveNoticeByCreatorId(Long creatorId) {
		return liveNoticeRepository.findLiveNoticeByCreatorId(creatorId);
	}

    

}
