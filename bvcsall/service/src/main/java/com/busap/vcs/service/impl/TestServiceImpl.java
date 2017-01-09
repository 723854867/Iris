package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Test;
import com.busap.vcs.data.repository.BaseRepository;
import com.busap.vcs.data.repository.TestRepository;
import com.busap.vcs.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by
 * User: djyin
 * Date: 12/12/13
 * Time: 9:58 AM
 */
@Service("testServiceImpl")
public class TestServiceImpl extends BaseServiceImpl<Test, Long> implements
        TestService {
    @Resource(name = "testRepository")
    private TestRepository testRepository;

    @Resource(name = "testRepository")
    @Override
    public void setBaseRepository(BaseRepository<Test, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
}
