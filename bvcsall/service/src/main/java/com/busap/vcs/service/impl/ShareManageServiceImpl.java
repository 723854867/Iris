package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.ShareManage;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.ShareManageRepository;
import com.busap.vcs.service.JedisService;
import com.busap.vcs.service.ShareManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Knight on 15/11/16.
 */
@Service("shareManageService")
public class ShareManageServiceImpl extends BaseServiceImpl<ShareManage, Long> implements ShareManageService {
    @Autowired
    JedisService jedisService;

    @Resource(name = "shareManageRepository")
    private ShareManageRepository shareManageRepository;

    @Resource(name = "shareManageRepository")
    @Override
    public void setBaseRepository(BaseRepository<ShareManage, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
}
