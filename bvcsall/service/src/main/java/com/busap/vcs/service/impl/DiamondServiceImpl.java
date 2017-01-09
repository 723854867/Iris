package com.busap.vcs.service.impl;

import com.busap.vcs.data.entity.Diamond;
import com.busap.vcs.data.mapper.DiamondDao;
import com.busap.vcs.data.model.DiamondDisplay;
import com.busap.vcs.service.DiamondService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by busap on 2015/12/23.
 */
@Service("diamondService")
public class DiamondServiceImpl implements DiamondService {

    @Resource
    private DiamondDao diamondDao;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return diamondDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Diamond record) {
        return diamondDao.insert(record);
    }

    @Override
    public Diamond selectByPrimaryKey(Long id) {
        return diamondDao.selectByPrimaryKey(id);
    }

    @Override
    public List<Diamond> selectAll(DiamondDisplay diamondDisplay) {
        return diamondDao.selectAll(diamondDisplay);
    }

    @Override
    public int updateByPrimaryKey(Diamond record) {
        return diamondDao.updateByPrimaryKey(record);
    }
}
